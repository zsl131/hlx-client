var mtTotalMoney = 0;
function buildMtHtml(obj) {
    var html = '<p class="type-name">'+$(obj).html() +'（'+$(obj).attr("title")+'）</small></p>'+
                '<div class="special-content">'+
                    '<div class="form-group">'+
                        '<div class="input-group input-group-lg">'+
                            '<div class="input-group-addon">美团编号：</div>'+
                            '<input name="mt-no" type="text" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" tabindex="1" maxlength="12" class="form-control" placeholder="输入美团编号，输完一个按回车，用扫码枪不用按回车" />'+
                        '</div>'+
                    '</div>'+

                    '<div class="show-nos-div"></div>'+

                '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);

    $("input[name='mt-no']").focus();

    $("input[name='mt-no']").keyup(function(event) {
        if(event.keyCode==13) {
            checkNo(this);
        }
    });

    mtTotalMoney = parseFloat($(".show-money").find(".money-amount").find(".money").html());

    //alert(mtTotalMoney);
}

function checkNo(obj) {
    var val = $(obj).val();
    if(!isExistsCode(val)) {
        var mtCount = getMtCount();
        var orderCount = getOrderCount();
        if(mtCount>=orderCount) {
            showDialog('美团编号数量请勿超过订单用餐人数', '<i class="fa fa-info-circle"></i> 系统提示');
        } else {
            if(val.length==12 && judgeIsNum(val)) {
                $.post("/meituan/handlerReady", {code:val}, function(res) {
                    //console.log(res);
                    if(res.code==0) {
                        showDialog(res.msg+'【<b style="color:#F00;">'+val+'</b>】', '<i class="fa fa-info-circle"></i> 系统提示');
                    } else {
                        //var orderSize = $(".commodity-list-hidden").find("span").length; //
                        //var
                        appendSingleCode(res.data);
                    }
                }, "json");
            }
        }
    } else {
        showDialog("美团券：<b style='color:#F00;'>"+val+"</b> 已经存在，请勿重复添加！", '<i class="fa fa-info-circle"></i> 系统提示');
    }
}

function appendSingleCodeInner(obj) {
    var mtCount = getMtCount();
    var orderCount = getOrderCount();
    if(mtCount>=orderCount) {
        showDialog('美团编号数量请勿超过订单用餐人数', '<i class="fa fa-info-circle"></i> 系统提示');
    } else {
    //让收银员操作验券张数
        var needCount = orderCount-mtCount; //还需要的美团券张数
        var maxCount = obj.count; //最大可验证张数
        if(maxCount==1) {
            onInputNum(obj, 1);
        } else {
            choiceCount(obj, mtCount, orderCount);
        }
    }
}

function appendSingleCode(obj) {
    var isDinner = isDinnerTime($("input[name='mealTime']").val());
//    var isDinner = false;
    var dealTitle = obj.dealTitle;
    if(!isDinner) {
        appendSingleCodeInner(obj);
    } else if(dealTitle.indexOf("晚餐")>=0 || dealTitle.indexOf("4人")>=0 || dealTitle.indexOf("双人")>=0) {
        appendSingleCodeInner(obj);
    } else {
        showDialog('晚上时间不能使用午餐券：'+dealTitle, '<i class="fa fa-info-circle"></i> 系统提示');
    }
}

function onInputNum(obj, count) {
    $(".show-nos-div").append(buildSingleCode(obj, count));
    calMtMoney();
    $("input[name='mt-no']").focus();
    $("input[name='mt-no']").val("");
}

function choiceCount(obj, mtCount, orderCount) {
    var needCount = orderCount - mtCount; //减
    if(needCount>obj.count) {needCount = obj.count;}
    var html = '<p>顾客有 <b style="color:#F00"> '+obj.count+' </b> 张券可消费，请选择消费张数</p>'+
                '<p>项目：'+obj.dealTitle+'[售价：'+obj.dealPrice+'元，最高抵用：'+obj.dealValue+'元]</p>'+
                '<p>消费者购买价：'+obj.dealPrice+'元</p>'+
                '<p>用餐人数：'+orderCount+'，已抵人数：'+mtCount+'，最大可用数：'+obj.count+'，还需数量：<b style="color:#F00">'+needCount+'</b></p>'+
                '<p class="choice-count-p" needCount="'+needCount+'"><button class="btn btn-sm btn-warning" onclick="changeCount(this)" amount="-1">-</button>&nbsp;<input value="'+needCount+'" name="choice-count" style="height:32px; width:50px; text-align:center;border:1px #ddd solid;" onkeyup="this.value=this.value.replace(/\\D/g,\'\')" readonly="readonly"/>&nbsp;<button class="btn btn-sm btn-warning" onclick="changeCount(this)" amount="1">+</button></p>'
                ;

    var myDialog = confirmDialog(html, '<i class="fa fa-eye"></i> 选择美团券码张数', function() {
        var realVal = parseInt($(myDialog).find("input[name='choice-count']").val());
        onInputNum(obj, realVal);
        $(myDialog).remove();
    });
}

function changeCount(obj) {
    var pObj = $(obj).parents("p.choice-count-p");
    var needCount = parseInt($(pObj).attr("needCount"));
    var curVal = parseInt($(pObj).find("input[name='choice-count']").val());
    var amount = parseInt($(obj).attr("amount"));
    var realVal = curVal + amount;
    if(realVal<=0) {realVal=1; }
    if(realVal>=needCount) {realVal = needCount;}
    $(pObj).find("input[name='choice-count']").val(realVal);
}

function buildSingleCode(obj, count) {
    var title = obj.dealTitle;
    var amount = 1;
    if(title.indexOf("4人")>=0) {amount = 4;}
    else if(title.indexOf("双人")>=0) {amount = 2;}
    var html = '<p class="single-code">'+
                '<b class="code" amount="'+amount+'">'+obj.couponCode+'</b>[<b style="color:#F00;" class="count">'+count+'</b>]'+
                '<span class="name">'+title+'</span>'+
                '</p>';

    return html;
}

//判断是否存在
function isExistsCode(code) {
    var exists = false;
    $(".show-nos-div").find(".single-code").each(function() {
        var c = $(this).find(".code").html();
        if(c==code) {
            exists = true;
        }
    });
    return exists;
}

function setReserveDatasByMt(val, title) {
    $("input[name='reserve']").val(val);
    $("input[name='reserve']").attr("title", title);
}

function judgeIsNum(srt){
    var pattern=/^\d+$/g;  //正则表达式 ^ 代表从开始位置起   $ 末尾   + 是连续多个  \d 是数字的意思
    var result= srt.match(pattern);//match 是匹配的意思   用正则表达式来匹配
    if (result==null){
        return false;
    }else{
        return true;
    }
}

//获取美团个数
function getMtCount() {
    var codeSize = 0;
    $(".show-nos-div").find(".single-code").each(function() {
        codeSize += parseInt($(this).find(".count").html());
    });
    return codeSize;
}

//获取订单人数
function getOrderCount() {
    var orderList = $(".commodity-list-hidden").find("span");
    return orderList.length;
}

function calMtMoney() {
    var codeSize = 0;
    var codes = '';
    $(".show-nos-div").find(".single-code").each(function() {
        codes += $(this).find(".code").html()+"_"+$(this).find(".count").html()+"_"+$(this).find(".code").attr("amount")+",";
        codeSize += parseInt($(this).find(".count").html());
    });
//    console.log("size:"+codeSize+"====codes:"+codes);
    var orderList = $(".commodity-list-hidden").find("span");
    //console.log(orderList.length);

    if(codeSize>orderList.length) {
        showDialog('美团编号数量请勿超过订单商品数量', '<i class="fa fa-info-circle"></i> 系统提示');
    } else {
        var needMoney = mtTotalMoney;
        for(var i=0;i<codeSize;i++) {
            var price = parseFloat($(orderList[i]).attr("price"));
//            console.log("price:"+price);
            needMoney -= price;
        }
        //console.log("================="+needMoney);
        $(".show-money").find(".money-amount").find(".money").html(needMoney);
        var bondMoney = parseFloat($(".show-money").find(".money-amount").find("small").attr("bondMoney"));
        if(needMoney-bondMoney<=0) {
            $(".pay-types").css("display", "none");
        } else {
            $(".pay-types").css("display", "block");
        }
        setReserveDatasByMt(codes, "可提交");
    }
}