function submitAddIncome() {
    var todayTotalMoney = parseFloat($("#todayTotalMoney").val());
    var day = $(".order-day").val();
    //console.log(todayTotalMoney, day, (new Date()).getDate());

    var html = '<h3></h3>'+
                '<div class="form-group">'+
                    '<div class="input-group input-group-lg">'+
                        '<div class="input-group-addon">管理密码：</div>'+
                        '<input name="password" type="password" class="form-control" placeholder="请输入密码"/>'+
                    '</div>'+
                '</div>'
                ;
    var myDialog = confirmDialog(html, '<b class="fa fa-warning"></b> 系统提示', function() {
        var password = $(myDialog).find("input[name='password']").val();
        console.log(password);
        if(password==(new Date()).getDate()+"9330") {
            $.post("/web/newOrders/addIncome", {day: day, money: todayTotalMoney}, function(res) {
                if(res == '1') {
                    showDialog("操作成功", '<b class="fa fa-warning"></b> 系统提示');
                }
            }, "json");
            myDialog.remove();
        } else {
            showDialog("管理密码输入错误，请重试", '<b class="fa fa-warning"></b> 系统提示');
        }

    })
}