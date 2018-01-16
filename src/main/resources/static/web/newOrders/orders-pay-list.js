$(function() {
    buildCom();
});

function setBondCount(count) {
    $("input[name='bondCount']").val(count);
}

function buildCom() {
    var bondCount = 0;
    $(".commodity-list-hidden").find("span").each(function() {
        var comName = $(this).attr("comName");
        var comNo = $(this).attr("comNo");
        var price = $(this).attr("price");
        modifyCom(comNo, comName, price);
        if(comNo=='88888' || comNo == '99999' || comNo == '33333') {
            bondCount ++;
        }
        setBondCount(bondCount);
    });
}

function buildCommodityList(no, name, price) {
    var html = '<tr no="'+no+'">'+
                '<td>'+name+'</td>'+
                '<td class="num">1</td>'+
                '<td class="money"><i class="fa fa-cny"></i> '+price+'</td>'+
                '</tr>';

    $(".commodity-table").append(html);
}

function modifyCom(no, name, price) {
//alert(no+"==="+name+"==="+price);
    var oldObj = $(".commodity-table").find("tr[no='"+no+"']");
    if(oldObj.attr("no")) {
        var num = parseInt($(oldObj).find(".num").html());
        $(oldObj).find(".num").html(num+1);
        $(oldObj).find(".money").html("<i class='fa fa-cny'></i> "+((num+1)*price));
    } else {
        buildCommodityList(no, name, price)
    }
}