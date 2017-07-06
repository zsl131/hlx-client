$(function() {
    $(".update-pay-type-href").click(function() {
        updatePayType($(this));
    });
});

function updatePayType(obj) {
    var orderNo = $("#orderNo").html();
    var field = $(obj).parents(".dropdown-menu").attr("field");
    var payType = $(obj).attr("val");
    var remark = $(obj).html();


    var html = '<i class="fa fa-question-circle"></i> 确定将此订单的【'+(field=='payType'?'就餐费用':'押金')+'支付方式】修改为：'+remark+'吗？';
    var myDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 修改支付方式提示', function() {
        $.post("/web/newOrders/updatePayType", {orderNo:orderNo, field:field, payType:payType}, function(res) {
            alert(res.msg);
            window.location.reload();
        }, "json");
    });
    //alert(orderNo+"==="+field+"==="+payType);
}