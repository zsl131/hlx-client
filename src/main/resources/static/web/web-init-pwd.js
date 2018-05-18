$(function() {
    $(".init-pwd-href").click(function() {
        var phone = $(this).attr("phone");
        var name = $(this).attr("name");
//        alert(phone);

        var html = '<div class="dialog-html-div">'+
                    '确定将【'+name+'】的支付密码初始化为【0000】吗？此操作必须征得顾客同意！'+
                    '</div>';
        var myDialog = confirmDialog(html, "<i class='fa fa-info'></i> 初始化支付密码", function() {
            $.post("/web/member/initPwd", {phone:phone}, function(res) {
                if(res=='1') {
                    showDialog("初始化密码成功", "<i class='fa fa-info-circle'></i>系统提示");
                    $(myDialog).remove();
                } else {
                    showDialog("初始化密码失败", "<i class='fa fa-info-circle'></i>系统提示");
                }
            }, "json");
        });
    });
})