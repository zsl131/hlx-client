$(function() {
    $(".pay-type-btns").find("button").click(function() {
        $(".pay-type-btns").find("button").each(function() {
            $(this).removeClass("btn-danger");
        });
        $(this).addClass("btn-danger");
        $("input[name='payType']").val($(this).attr("payType"));
    });


});

function checkData() {
    var type = $('input:radio[name="levelId"]:checked').val();
    var name = $("input[name='name']").val();
    var phone = $('input[name="phone"]').val();
    var payType = $("input[name='payType']").val();
    if(!type) {
        showDialog("请选择会员种类");
    } else if($.trim(name)=='') {
        showDialog("请输入顾客姓名，方便消费时核对");
    } else if(!isPhone(phone)) {
        showDialog("请输入顾客手机号码，消费时需要顾客提供该手机号码进行验证");
    } else if($.trim(payType)=='') {
        showDialog("请选择支付方式");
    } else {
        return true;
    }
    return false;
}

function isPhone(sMobile) {
    /*if((/^1[3|4|5|7|8][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}*/

    if(sMobile.length!=11 || !/^[0-9]+$/.test(sMobile)){
        return false;
    }
    return true;
}