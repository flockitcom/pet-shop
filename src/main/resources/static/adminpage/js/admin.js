layui.use(["element", "layer"], function () {
    var $ = layui.jquery,
        element = layui.element(),
        layer = layui.layer;
    var l_o = $(".left-menu"),
        tab = "top-tab",
        l_m = "left-menu",
        t_m = "top-menu";
    var mainHeight = $(window).height() - 60 - 41 - 44 - 5;
    element.on("nav(" + t_m + ")", function (data) {
        l_o.hide().eq(data.index()).show()
    });
    l_o.on("click", "li", function () {
        $(this).siblings().removeClass("layui-nav-itemed")
    });
    element.on("nav(" + l_m + ")", function (data) {
        var a_t = data.children("a"),
            id = a_t.data("id"),
            url = a_t.data("url"),
            title = a_t.html(),
            length = $(".layui-tab-title").children("li[lay-id='" + id + "']").length;
        if (!length) {
            var iframe = '<iframe src="' + url + '" style="height:' + mainHeight + 'px;"></iframe>';
            element.tabAdd(tab, {
                title: title,
                content: iframe,
                id: id
            })
        }
        element.tabChange(tab, id)
    });
    l_o.children("li:first").children("a:first").click();
    $(".menu-flexible").click(function () {
        $(".left-menu-all").toggle();
        $(".layui-body,.layui-footer").css("left", ($(".left-menu-all").is(":hidden")) ? "0" : "200px")
    });
    $(".layui-tab-button").on("click", "a", function () {
        var iframe = $(".layui-tab-content").find(".layui-show iframe");
        iframe.attr("src", iframe.attr("src"))
    })
});