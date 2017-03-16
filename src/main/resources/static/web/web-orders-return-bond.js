$(function() {
    $(".search-order-btn").click(function() {
        var no = $("input[name='orderNo']").val();
        window.location.href='/web/orders/returnBond?no='+no;
    });
});

function returnBond(obj) {
    var no = $(obj).attr("no"); //订单编号
    var bondMoney = parseFloat($(obj).attr("bondMoney")); //顾客所交压金
    var ordersStatus = $("#ordersStatus").val();
    if(ordersStatus!='2' && ordersStatus!='3') {
        showDialog('只有在就餐状态才可以退还压金！', '<i class="fa fa-info-circle"></i> 系统提示');
    } else {
        var html = '<h4>顾客所交压金为：<b style="color:#F00;">'+bondMoney+'</b> 元。</h4>'+
                '<input name="money" type="text" onKeyUp="value=value.replace(/[^\\d]/g,\'\')" class="form-control" placeholder="输入所扣压金，不扣请输入0" />';
        var returnBondDialog = confirmDialog(html, "<i class='fa fa-flask'></i> 退还压金", function() {
            var money = parseFloat($(returnBondDialog).find("input[name='money']").val());
            if((money!=0 && !money) || money<0 || money>bondMoney) {
                showDialog("请输入所扣压金，不扣请输入0，且金额不得大于"+bondMoney+" 元", "<i class='fa fa-info-circle'></i> 系统提示");
            } else {
                $.post("/web/orders/returnBond", {no:no, money:money}, function(res) {
                    alert(res.msg);
                    window.location.reload();
                }, "json");
            }
        });
    }
}
