function buildFriendHtml(obj) {
    var html = '<p class="type-name">'+$(obj).html() +'<small>（'+$(obj).attr("title")+'）</small></p>'+
                    '<div class="special-content">'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">折扣电话：</div>'+
                                '<input name="bossPhone" type="text" maxlength="11" tabindex="1" onKeyUp="onBossPhone(this)" class="form-control" placeholder="由顾客提供有折扣权限的电话号码" />'+
                                '<span class="input-group-addon show-member-name">请输入</span>'+
                            '</div>'+
                        '</div>'+
                        '<div class="form-group">'+
                            '<div class="input-group">'+
                                '<div class="input-group-addon">对应姓名：</div>'+
                                '<input class="form-control" name="bossName" value="输入会员电话检索" readonly="readonly"/>'+
                            '</div>'+
                        '</div>'+
                        '<div class="member-info-show"></div>'+
                    '</div>';

    $(".special-type").css("display", "block");
    $(".special-type").html(html);
    $("input[name='bossPhone']").focus();
}

function onBossPhone(obj) {
    $(obj).val($(obj).val().replace(/[^\d]/g,''));
    //alert($(obj).attr("name"));
    checkBossPhone();
}

function checkBossPhone() {
    var phoneObj = $("input[name='bossPhone']");
    var phone = $(phoneObj).val();
    if(isPhone(phone)) {
        $.post("/public/json/getAdminPhone", {phone:phone}, function(res) {
            if(res.id) {
                var html = res.name;
                $("input[name='bossName']").val(html);
                $(".show-member-name").html("检索成功");
//                setReserveInfo(res.phone, "可以提交");
                sendOrder2Boss(res);
            } else {
                //showDialog("<i class='fa fa-close'></i> 输入的手机号码【"+phone+"】查无会员信息！");
                $(".show-member-name").html("此号码无折扣权限");
                $(".show-member-name").css({'color':'#F00',"font-weight":'bold'});
            }
        }, "json");
    } else {
        $(".show-member-name").html("请认真输入");
        $("input[name='bossName']").val("-");
    }
}

function sendOrder2Boss(res) {
    var html = '<h4>折扣号码已检索成功！</h4>'+
                '<p>折扣号码：'+res.phone+'</p>'+
                '<p>对应姓名：'+res.name+'</p>'+
                '<p>是否现在将订单提交给【'+res.name+'】进行折扣确认？</p>'+
                '<p>只有【'+res.name+'】确认后才可享受折扣价。</p>';
    var myDialog = confirmDialog(html, '<i class="fa fa-info-circle"></i> 订单确认提示', function() {
        var orderNo = $("input[name='orderNo']").val();
        $.post("/web/newOrders/sendFriendOrder", {no:orderNo, bossPhone:res.phone}, function(res) {
            alert(res.msg);
            window.location.reload();
        }, "json");
    });
}