$(function() {
    //setRetreatBtn();
});

//点击退票
function submitRetreat() {
    var ordersNo = $("#ordersNo").val(); //订单编号
    var html = '<input name="reason" type="text" class="form-control" placeholder="请输入退票原因" />';
    var retreatDialog = confirmDialog(html, "<i class='fa fa-info-circle'></i> 退票提示", function() {
        var reason = $(retreatDialog).find("input[name='reason']").val();
        if($.trim(reason)=='') {showDialog("请认真输入退票原因", "<i class='fa fa-info-circle'></i> 系统提示");}
        else {
            $.post("/web/orders/retreatOrders", {no:ordersNo, reason:reason}, function(res) {
                alert(res.msg);
                if(res.code=='0') {
                    window.location.reload();
                }
            }, "json");
        }
    });
}

function onRetreatBtnClick() {
    var entryLong = $("#entryLong").val(); //入场Long
    var refundMin = parseInt($("#refundMin").val()); //入场退票时间分钟
    var curDateTime = new Date().getTime();
    var diffSeconds = parseInt((curDateTime-entryLong)/1000);
    var canRefund = (diffSeconds-(refundMin*60))<=0;
    var ordersType = $("#ordersType").val(); //订单类型
    var ordersStatus = $("#ordersStatus").val(); //订单状态
    var btnObj= $(".retreat-btn");
    //alert(canRefund+"==="+ordersType+"==="+diffSeconds+"==="+refundMin);
    //在这里全额退款有几个条件：1、在允许的时间内；2、订单类型为“店内订单”或“友情价订单”；3、状态为就餐状态
    if(canRefund && (ordersType=='1' || ordersType=='4') && ordersStatus=='2') {
        //alert("可以退票");
        //$(btnObj).removeAttr("disabled");
        submitRetreat(); //可以退票
    } else {
        //alert("不可以");
        //$(btnObj).attr("disabled", "disabled");
        //$(btnObj).attr("title", "入场超过"+refundMin+"分钟，禁止退票");
        cannotRetreat();
    }
}

function cannotRetreat() {
    var refundMin = parseInt($("#refundMin").val()); //入场退票时间分钟
    var html = '<p style="color:#F00">此订单不可退票！退票必须同时满足以下条件：</p>'+
                '<p>1、在入场后'+refundMin+'分钟以内；</p>'+
                '<p>2、订单类型为“店内订单”或“友情价订单”</p>'+
                '<p>3、订单状态必须为“就餐中”</p>';

    showDialog(html, "<i class='fa fa-info-circle'></i> 系统提示");
}

function onCancelBtnClick() {
    var ordersType = $("#ordersType").val(); //订单类型
    var ordersStatus = $("#ordersStatus").val(); //订单状态
    if(ordersType!='4' || (ordersStatus!='0' && ordersStatus!='6')) {
        cannotCancel();
    } else {
        submitCancel();
    }
}

//点击取消订单
function submitCancel() {
    var ordersNo = $("#ordersNo").val(); //订单编号
    var html = '<input name="reason" type="text" class="form-control" placeholder="请输入取消订单的原因" />';
    var retreatDialog = confirmDialog(html, "<i class='fa fa-info-circle'></i> 退票提示", function() {
        var reason = $(retreatDialog).find("input[name='reason']").val();
        if($.trim(reason)=='') {showDialog("请认真输入取消订单的原因", "<i class='fa fa-info-circle'></i> 系统提示");}
        else {
            $.post("/web/orders/cancelOrders", {no:ordersNo, reason:reason}, function(res) {
                alert(res.msg);
                if(res.code=='0') {
                    window.location.reload();
                }
            }, "json");
        }
    });
}

function cannotCancel() {
    var html = '<p style="color:#F00">此订单不可取消！取消订单必须同时满足以下条件：</p>'+
                '<p>1、状态只能为刚下单状态</p>'+
                '<p>2、订单类型为“友情价订单”</p>';

    showDialog(html, "<i class='fa fa-info-circle'></i> 系统提示");
}
