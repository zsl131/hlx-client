$(function() {
});

function checkData() {
    var type = $('input:radio[name="levelId"]:checked').val();
    var name = $("input[name='name']").val();
    var phone = $('input[name="phone"]').val();
    if(!type) {
        showDialog("请选择会员种类");
    } else if($.trim(name)=='') {
        showDialog("请输入顾客姓名，方便消费时核对");
    } else if(!isPhone(phone)) {
        showDialog("请输入顾客手机号码，消费时需要顾客提供该手机号码进行验证");
    } else {
        return true;
    }
    return false;
}

function isPhone(sMobile) {
    if((/^1[3|4|5|8][0-9]\d{8}$/.test(sMobile))) {
        return true;
    } else {return false;}
}