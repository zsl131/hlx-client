$(function() {
    $(".order-type-div").find("button").click(function() {
        onOrderType($(this));
    });
    $(".pay-types").find("button").click(function() {
        onPayType($(this));
    });
    onOrderType($(".order-type-div").find("button[orderType='1']"));
});

function onOrderType(obj) {
    $(".order-type-div").find("button").each(function() {
        $(this).removeClass("btn-info")
    });

    $(obj).addClass("btn-info");

    setPayInfo(obj);
}

function setPayInfo(obj) {
    var orderType = $(obj).attr("orderType");
    $("input[name='specialType']").val(orderType);
    var title = $(obj).attr("title");
    $(".special-type").html(title);
    if(orderType!='1') {
        $(".pay-types").css("display", "none");
        $(".submit-order-btn").html('<i class="fa fa-check"></i> 确认提交');
    } else {
        $(".pay-types").css("display", "block");
        $(".submit-order-btn").html('<i class="fa fa-check"></i> 确认收款');
    }
}

//String no, Float bondMoney, Integer bondCount, String payType,
//                                          String specialType, String reserve
function submitOrder() {
    var no = $("input[name='orderNo']").val();
//    var payType = $("input[name='payType']").val();
    var payType = $(".pay-types").find("button.btn-danger").attr("payType");
    var specialType = $("input[name='specialType']").val();

    var html = '<i class="fa fa-info"></i> 确定已收款并提交订单吗？';
    if(specialType!='1') {
        html = '<i class="fa fa-info"></i> 确定信息完善并现在提交吗？';
    }
    var submitDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 系统提示', function() {
        $.post("/web/newOrders/postOutOrder", {no:no, payType:payType, specialType:specialType}, function(res) {
            alert(res.msg);
            window.location.reload();
        }, "json");

        $(submitDialog).remove(); //直接关闭
    });
}

function onPayType(obj) {
    $(".pay-types").find("button").each(function() {
        $(this).removeClass("btn-danger");
    });
    $(obj).addClass("btn-danger");
    $("input[name='payType']").val($(obj).attr("payType"));
}
