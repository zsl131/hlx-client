$(function() {
    $("input[name='orderNo']").focus();
    $(".search-order-btn").click(function() {
        searchOrder();
    });
    $("input[name='orderNo']").keyup(function(event) {
        if(event.keyCode==13) {
            searchOrder();
        }
    });
});

function searchOrder() {
    var no = $("input[name='orderNo']").val();
    window.location.href='/web/newOrders/receiveMoney?no='+no;
}

function receiveMoney(obj) {
    var no = $(obj).attr("no"); //订单编号
    var ordersStatus = $("#ordersStatus").val();
    if(ordersStatus!='1' ) {
        showDialog('<i class="fa fa-info"></i> 只有在配送状态才需要完成外卖订单收款！', '<i class="fa fa-info-circle"></i> 系统提示');
    } else {
        var html = '<div class="pay-types"><span>支付方式：</span>'+
                    '<div class="btn-group" role="group" aria-label="...">'+
                        '<button type="button" onclick="choicePayType(this)" class="btn btn-default btn-danger" payType="1"><i class="fa fa-cny"></i> 现金支付</button>'+
                        '<button type="button" onclick="choicePayType(this)" class="btn btn-default" payType="3"><i class="fa fa-weixin"></i> 微信支付</button>'+
                        '<button type="button" onclick="choicePayType(this)" class="btn btn-default" payType="4">支付宝支付</button>'+
                        '<button type="button" onclick="choicePayType(this)" class="btn btn-default" payType="2"><i class="fa fa-credit-card-alt"></i> 刷卡支付</button>'+
                    '</div></div>';
        var returnBondDialog = confirmDialog(html, "<i class='fa fa-flask'></i> 外卖收款", function() {
            var payType = $(returnBondDialog).find(".pay-types").find("button.btn-danger").attr("payType");
             $.post("/web/newOrders/receiveMoney", {no:no, payType:payType}, function(res) {
                alert(res.msg);
                window.location.reload();
             }, "json");
        });
    }
}

function choicePayType(obj) {
    $(obj).parents(".pay-types").find("button").each(function() {
        $(this).removeClass("btn-danger");
    });
    $(obj).addClass("btn-danger");
}
