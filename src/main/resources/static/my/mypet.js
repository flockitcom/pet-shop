var pet = "http://127.0.0.1:80";
// var pet = "http://116.62.50.16:80";

//用JS获取地址栏参数的方法（超级简单）
function GetQueryString(name)
{
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)","i");
    var r = window.location.search.substr(1).match(reg);//search,查询？后面的参数，并匹配正则
    if (r != null) return unescape(r[2]);
    return null;
}

