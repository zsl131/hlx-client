$(function() {
    queryPrice();
});

function queryPrice() {
    $.get("/public/json/getPrice",{},function(res) {
        if(res) {
            $("b.breakfastPrice").html(res.breakfastPrice);
            $("b.dinnerPrice").html(res.dinnerPrice);
        }
    }, "json");
}