layui.use(['form', 'upload', 'layer', 'laytpl', 'jquery'], function () {

    var $ = layui.$,
        form = layui.form,
        upload = layui.upload,
        laytpl = layui.laytpl,
        layer = layui.layer;
    $(function () {
        $.ajax({
            url: PageContext.getUrl("/deviceInfo/detail/" + $("#id").val()),
            type: 'post',
            async: false,
            success: function (data) {
                // alert(data)
                // console.log(data);
                if (data.flag === "true") {
                    var getTpl = document.getElementById('detailView').innerHTML
                        , view = document.getElementById('view');
                    laytpl(getTpl).render(data.data, function (html) {
                        view.innerHTML = html;
                    });

                }

            }
        });
    });
});