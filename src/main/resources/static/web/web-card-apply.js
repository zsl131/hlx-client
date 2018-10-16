$(function() {
    $(".resubmit-href").click(function() {
        var objId = $(this).attr("objId"); var cardNo = $(this).attr("cardNo");
        console.log(objId, cardNo)

        var html = '<div>确定重新提交申请吗？【'+cardNo+'】</div>';
        var myDialog = confirmDialog(html, '<b class="fa fa-ticket"></b> 重新提交卡券申请', function() {
            $.post("/web/card/resubmitCardApply", {id: objId}, function(res) {
                if("1"==res) {
                    showDialog("申请成功，等待审核", "系统提示");
                }
                $(myDialog).remove(); //关闭窗口
            }, "json");
        });
    })
})