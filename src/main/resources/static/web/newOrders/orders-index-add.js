$(function() {
    $(".submit-pay-btn").click(function() {
        submit2Pay();
    });
});

function submit2Pay() {
    var orderNos = $("input[name='orderNos']").val();
    var totalCount = $(".submit-orders-div").find(".count").find("b").html();
    var totalMoney = parseFloat($(".submit-orders-div").find(".money").find("b").html());
    if(orderNos==null || $.trim(orderNos)=='' || orderNos=='0,0' || totalMoney<=0) {
        var html = '<p>此订单无法提交（原因可能如下）：</p>'+
                    '<p>1、订单中无任何商品；</p>'+
                    '<p>2、订单总金额为0。</p>';
        showDialog(html);
    } else {
        var html = '<p>确定提交此订单吗？</p>'+
                    '<p>此订单商品总数为：'+totalCount+' ，订单总金额为：'+totalMoney+' 元</p>';
        var myDialog = confirmDialog(html, '<i class="fa fa-question-circle"></i> 确定提交订单吗？', function() {
            $.post("/web/newOrders/addBuffetOrder", {commodityNo:orderNos}, function(res) {
                var code = res.code;
                if(code>3) {
                    window.location.href = '/web/newOrders/payBuffetOrder?orderNo='+code;
                } else {
                    alert(res.msg);
                }
                $(myDialog).remove();
            }, "json");
        });
    }
}

function addCommodity2Order(obj) {
    var canBuy = $(obj).attr("canBuy");
    var no = $(obj).find(".name").attr("no");
    if(canBuy==1) { //只有canBuy属性为true时可以下单
        var name = $(obj).find(".name").html();
        var price = $(obj).find(".price").html();
        var no = $(obj).find(".name").attr("no");
        //alert(no+"======="+name+"-------"+price);
        addCommodity(name,price,no);
        $(".index-commodity-list").find("ul").find("li").each(function() {
            var curNo = $(this).find(".name").attr("no");
            if((no == '33333' || no =='22222') && (curNo != '33333' && curNo !='22222')) {
                setComStyle(this, 0);

            } else if((no != '33333' && no != '22222') && (curNo == "33333" || curNo == '22222')) {
                setComStyle(this, 0);
            }
        })
    } else {
        var name = $(obj).find(".name").html();
        showDialog("该商品【"+name+"】不可在此时段下单购买！");
    }
}

function addCommodity(name, price, no) {
    var listObj = $(".shopping-list");
    queryCommodity(listObj, name, price, no);
}

function queryCommodity(listObj, name, price, no) {
    var oldCom = $(listObj).find(".single-commodity[no='"+no+"']");
    var oldNo = oldCom.attr("no");
    if(oldNo){
        modifyNum(oldCom, 1);
    } else {
        var singleObj = buildSingleCommodity(name, price, no);
        $(listObj).append(singleObj);
        setWidth($(listObj).find(".single-commodity"));
    }
    buildSubmitData();
}

function modifyNum(obj, numFlag) {
    var oldNumObj = $(obj).find(".left").find(".center").find(".num").find("b");
    var totalObj = $(obj).find(".right").find("b");
    var oldNum = parseInt($(oldNumObj).html());
    var price = parseFloat($(obj).attr("price"));
    var total = parseFloat($(totalObj).html());
    var curNum = oldNum + numFlag;
    if(curNum<=0) {
        removeSingle(obj);
    } else {
        $(oldNumObj).html(curNum);
        $(totalObj).html(curNum*price);
    }
    buildSubmitData();
}

function removeSingle(obj) {
    var name = $(obj).find(".left").find(".center").find(".name").html();
    var html = '确定从已选商品中删除【'+name+'】吗？删除后可以再次从商品列表中选择。';

    var myDialog = confirmDialog(html, '<i class="fa fa-question-circle"></i> 确定删除此商品吗？', function() {
        $(obj).remove();
        $(myDialog).remove();
        buildSubmitData();
    });
}

function stageOrder() {
    showDialog("功能完善中，敬请期待……");
}

function cleanAll() {
    var html = '确定清空所有已选商品吗？清空后可以再次从商品列表中选择。';

    var myDialog = confirmDialog(html, '<i class="fa fa-question-circle"></i> 确定清空所有已选商品吗？', function() {
        $(".shopping-list").html("");
        $(myDialog).remove();
        buildSubmitData();
    });
}

function changeCom(obj, amount) {
    var comObj = $(obj).parents(".single-commodity");
    modifyNum(comObj, amount);
}

function buildSingleCommodity(name, price, no) {
    var html = '<div class="single-commodity" no="'+no+'" price="'+price+'">' +
                    '<div class="left">' +
                        '<span class="minus" title="数量减1" onclick="changeCom(this, -1)"><b class="fa fa-minus"></b></span>' +
                        '<span class="center">' +
                            '<p class="name" title="商品名称">'+name+'</p>' +
                            '<p class="num" title="单品数量">X <b>1</b></p>' +
                        '</span>' +
                        '<span class="plus" title="数量加1" onclick="changeCom(this, 1)"><b class="fa fa-plus"></b></span>' +
                    '</div>' +
                    '<div class="right" title="单品小计"><i class="fa fa-cny"></i> <b>'+price+'</b></div>' +
                '</div>';

    return html;
}

function buildSubmitData() {
    var totalCount = 0;
    var totalMoney = 0;
    var nos = '0,';
    $(".shopping-list").find(".single-commodity").each(function() {
        var count = parseInt($(this).find(".left").find(".center").find(".num").find("b").html());
        var money = parseFloat($(this).find(".right").find("b").html());
        var no = $(this).attr("no");
        totalCount += count;
        totalMoney += money;
        for(var i=0;i<count;i++) {
            nos += no+",";
        }
    });

    $(".submit-orders-div").find("input[name='orderNos']").val(nos+='0');
    $(".submit-orders-div").find(".count").find("b").html(totalCount);
    $(".submit-orders-div").find(".money").find("b").html(totalMoney);
}