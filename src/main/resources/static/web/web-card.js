$(function() {
    $(".apply-card-btn").click(function() {
        var canSubmitCard = false;
        var html = '<div class="card-main-content">努力加载中……</div>';
        var myDialog = confirmDialog(html, '<b class="fa fa-ticket"></b> 申请卡券', function() {
            if(canSubmitCard) {
                var type = $(myDialog).find('input[name="type"]').val();
                var reason = $(myDialog).find('input[name="reason"]').val();
                if(!type || !reason) {showDialog("请选择原因和类型", "系统提示");} else {
                    $.post("/web/card/applyCard", {type: type, reason: reason}, function(res) {
                        if(res=='-1') {showDialog("无权限操作", "系统提示");} else if (res == '0') {
                            showDialog("无卡券", "系统提示");
                        } else if(res=='1') {
                            showDialog("申请成功，等待审核", "系统提示");
                        }
                    }, "json");
                    $(myDialog).remove(); //关闭窗口
                }
            } else {showDialog("请先点击“检索卡券”", "系统提示");}
        }, "static");
        $.post("/web/card/listReason",{},function(res) {
            var opts = '';
            for(var i=0;i<res.length;i++) {
                opts += '<option value="'+res[i].name+'">'+res[i].name+'</option>';
            }
            html ='<div class="form-group">'+
                    '<div class="input-group">'+
                        '<div class="input-group-addon">原因：</div>'+
                            '<select class="form-control" onChange="setReason(this)">'+
                              '<option value="">==选择申请原因==</option>'+opts+
                            '</select>'+
                        '<input name="reason" type="hidden"/>'+
                    '</div>'+
                '</div>'+
                '<div class="form-group"><div class="input-group">'+
                     '<div class="input-group-addon">类型：</div>'+
                     '<div class="btn-group " role="group" aria-label="...">'+
                         '<button type="button" class="btn btn-default card-type-btn" typeValue="1">10元券</button>'+
                         '<button type="button" class="btn btn-default card-type-btn" typeValue="2">45元券</button>'+
                         '<button type="button" class="btn btn-default card-type-btn" typeValue="3">55元券</button>'+
                     '</div>'+
                     '<input name="type" type="hidden"/>'+
                 '</div></div>'+
                '<div class="form-group">'+
                        '<div class="input-group">'+
                            '<div class="input-group-addon">卡号：</div>'+
                            '<div class="form-control card-no-div">点击“检索卡券”</div>'+
                        '</div>'+
                    '</div>'+
                    '<div class="form-group">'+
                        '<button type="button" class="btn btn-primary check-nos-btn" >检索卡券</button>'+
                    '</div><div class="form-group show-nos" style="width:100%;float:left;"></div>';
            $(myDialog).find(".card-main-content").html(html);

            $(".check-nos-btn").click(function() {
                var type = $(myDialog).find('input[name="type"]').val();
                if(!type) {showDialog("请选择类型", "系统提示");} else {
                    $.post("/web/card/findCardNo", {type:type}, function(res) {
                        var html = '';
                        if(!res) {html = '未检索到任何卡信息'; canSubmitCard = false;} else {
                            html =res;
                            canSubmitCard = true;
                        }
                        $(myDialog).find(".card-no-div").html(html);
                    }, "json");
                }
            })

            $(".card-type-btn").click(function() {
                $(".card-type-btn").removeClass("btn-primary");
                $(this).addClass("btn-primary");
                var val = $(this).attr("typeValue");
                $(myDialog).find("input[name='type']").val(val);
            })
        }, "json");
    });
})

function setReason(obj) {
    $(obj).parents(".card-main-content").find("input[name='reason']").val(obj.value);
}