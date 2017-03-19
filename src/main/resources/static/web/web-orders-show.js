
//友情价订单，确认收款
function onGotMoney() {
    var orderNo = $("#ordersNo").val(); //订单编号
    var bondMoney = parseFloat($(".bond-money").html());
    var totalMoney = parseFloat($(".total-money").html());
    var discountMoney = parseFloat($(".discount-money").html());
    var html = '<p style="color:#F00">确定已经收到就餐费用了吗？</p>'+
                '<p>就餐费用：'+totalMoney+' 元</p>'+
                '<p>折扣金额：'+discountMoney+' 元</p>'+
                '<p>压金金额：'+bondMoney+' 元</p>'+
                '<b style="color:#F00">应收费用：'+(totalMoney+bondMoney)+' 元</b>（含压金）'+
                '<div style="margin-top:10px;" class="dialog-pay-type">付款方式：'+
                '<button class="btn" value="1" onclick="setPayType(this)">现金</button>&nbsp;&nbsp;'+
                '<button class="btn" value="2" onclick="setPayType(this)">刷卡</button>&nbsp;&nbsp;'+
                '<button class="btn" value="3" onclick="setPayType(this)">微信</button>&nbsp;&nbsp;'+
                '<button class="btn" value="4" onclick="setPayType(this)">支付宝</button>'+
                '<input type="hidden" name="payType"/>'+
                '</div>';
    var moneyDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
        var payType = $(moneyDialog).find("input[name='payType']").val();
        if($.trim(payType)=='') {
            showDialog("请选择顾客付款方式。");
        } else {

            $.post("/web/orders/confirmOrder", {no:orderNo, payType:payType}, function(res) {
                alert(res.msg);
                window.location.reload();
            }, "json");
        }
    });
}

function setPayType(obj) {
    var divObj = $(obj).parents(".dialog-pay-type");
    var val = $(obj).val();
    $(divObj).find("button").removeClass("btn-info");
    $(obj).addClass("btn-info");
    $(divObj).find("input[name='payType']").val(val);
}