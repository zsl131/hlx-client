$(function() {
    checkOrderStatus();
});

function checkOrderStatus() {
    var ordersStatus = $("#ordersStatus").val();
    var ordersType = $("#ordersType").val();
    //alert(ordersStatus+"==="+ordersType);
    if(ordersStatus=='0' && ordersType=='4') {
        setInterval("checkOrderInterval()", 1000);
    }
}

function checkOrderInterval() {
    var orderNo = $("#ordersNo").val();
    $.post("/web/orders/queryOrder", {no:orderNo}, function(order) {
        if(order.status=='6' || order.status=='-3') { //老板已经确认，可以付款
            window.location.reload();
        }
    }, "json");
}