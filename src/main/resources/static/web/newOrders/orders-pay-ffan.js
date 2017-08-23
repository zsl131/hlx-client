var ffanTotalMoney = 0;
function buildFfanHtml(obj) {
    var html = '<p class="type-name">'+$(obj).html() +'（'+$(obj).attr("title")+'）</small></p>'+
                '<div class="special-content">'+
                    '<div class="form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">飞凡提货码或券码：</div>'+
                            '<input name="ffan-no" type="text"  tabindex="1" class="form-control" placeholder="输入飞凡提货码或券码，输完一个按回车，用扫码枪不用按回车" />'+
                        '</div>'+
                    '</div>'+

                    '<div><button class="btn btn-info" onclick="addOneCodeFfan()"><i class="fa fa-plus"></i> 添加一位</button>（当存在飞凡码是多人套餐时才使用此按钮）</div>'+

                '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);

    $("input[name='ffan-no']").focus();
    checkFfanNo();

    ffanTotalMoney = parseFloat($(".show-money").find(".money-amount").find(".money").html());

    //alert(mtTotalMoneyNoNet);
}

function addOneCodeFfan() {
    //calMtMoneyNoNet();
    var nos = $('input[name="ffan-no"]').val();
    $('input[name="ffan-no"]').val(nos+buildFfanSingleCode()+",");

    rebuildFfanNo();
    calFfanMoney();
}

function buildFfanSingleCode() {
    var code = "777777";
    for(var i=0;i<6;i++) {
        var d = parseInt(Math.random()*10);
        code += d+"";
    }
    return code;
}

function calFfanMoney() {
    var nos = $('input[name="ffan-no"]').val();
    var array = nos.split(",");
    //console.log(array.length);

    var orderList = $(".commodity-list-hidden").find("span");
    //console.log(orderList.length);

    if(array.length-1>orderList.length) {
        showDialog('提货码或券码数量请勿超过订单商品数量', '<i class="fa fa-info-circle"></i> 系统提示');
    } else {
        var needMoney = ffanTotalMoney;
        for(var i=0;i<array.length-1;i++) {
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
        setReserveDatasByFfan(array.join(","), "可提交");
    }
}

function checkFfanNo() {
    $('input[name="ffan-no"]').keyup(function(e) {
        if(e.keyCode==13) {
            $(this).val($(this).val()+",");
            rebuildFfanNo();

            calFfanMoney();
        }
    });
}

function rebuildFfanNo() {
    var result = '';
    var val = $('input[name="ffan-no"]').val();
    var array = val.split(",");
    for(var i=0;i<array.length;i++) {
        var no = array[i];
        no = $.trim(no);
        if(judgeIsNumFfan(no)) {
            result+=no+",";
        }
    }

    array = result.split(",");
    var newArray = [];
    for(var i=0;i<array.length;i++) {
        var d = array[i];
        if($.trim(d)!=''){
            if($.inArray(d, newArray)<0) {
                newArray.push(d);
            }
        }
    }

    var orderSize = $(".commodity-list-hidden").find("span").length;
    var arraySize = newArray.length;
    var needSize = arraySize;
    if(arraySize>orderSize) {
        needSize = orderSize;
        showDialog('<p>飞凡提货码或券码数量请勿超过订单商品数量</p><p>已自动移除多余的编码</p>', '<i class="fa fa-info-circle"></i> 系统提示');
    }
    var res = '';
    for(var i=0;i<needSize;i++) {
        res+=newArray[i]+",";
    }
    $('input[name="ffan-no"]').val(res);
}

function judgeIsNumFfan(srt){
    var pattern=/^\d+$/g;  //正则表达式 ^ 代表从开始位置起   $ 末尾   + 是连续多个  \d 是数字的意思
    var result= srt.match(pattern);//match 是匹配的意思   用正则表达式来匹配
    if (result==null){
        return false;
    }else{
        return true;
    }
}

function setReserveDatasByFfan(val, title) {
    $("input[name='reserve']").val(val);
    $("input[name='reserve']").attr("title", title);
}