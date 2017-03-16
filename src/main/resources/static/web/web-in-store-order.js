$(function() {
    $("input[name='peopleCount']").focus();

    $(".normal-order-btn").click(function() {
        submitNormalOrder(this);
    });
});

function submitNormalOrder(obj) {
    var pObj = $(obj).parents(".main-form-box");
    //alert($(pObj).html());
    var price = getPrice(); //单价
    var peopleCount = parseInt($(pObj).find("input[name='peopleCount']").val()); //成人数
    var halfCount = parseInt($(pObj).find("input[name='halfCount']").val()); //半票人数
    var childCount = parseInt($(pObj).find("input[name='childCount']").val());

    var payType = $(pObj).find("input[name='payType']").val();
    var bondMoney = parseFloat($("b.bondMoney").html())*peopleCount;
    var level = $("#isDinner").val()=='1'?"1":"2"; //晚餐或午餐

    //alert("price:"+price+";peopleCount:"+peopleCount+";childCount:"+childCount+";payType:"+payType+";bondMoney:"+bondMoney);
    if(peopleCount<=0) {
        showDialog("请输入全票人数", "<i class='fa fa-info-circle'></i> 系统提示");
    } else if((halfCount!=0 && !halfCount) || halfCount<0) {
        showDialog("请输入半票人数，没有请输入0", "<i class='fa fa-info-circle'></i> 系统提示");
    } else if((childCount!=0 && !childCount) || childCount<0) {
        showDialog("请输入儿童人数，没有请输入0", "<i class='fa fa-info-circle'></i> 系统提示");
    } else if(!payType) {
        showDialog("请选择顾客付款方式", "<i class='fa fa-info-circle'></i> 系统提示");
    } else {
        setSubmitBtnStyle(obj, true);
        $.post("/web/orders/addInStoreOrder", {peopleCount:peopleCount, halfCount:halfCount, childCount:childCount, payType:payType, level:level, price:price, bondMoney:bondMoney}, function(res) {
            if(res=='1') {
                alert("订单已完成，请顾客带上小票入场就餐！");
                window.location.reload();
            } else if(res=='-1') {
                alert("未检测到收银员，即将刷新……");
                window.location.reload();
            } else if(res=='-2') {
                alert("未知错误！");
            }
            setSubmitBtnStyle(obj, false);
        }, "json");
    }
}

