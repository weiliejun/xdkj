layui.use(['form', 'layer', 'laydate', 'jquery', 'laytpl', 'upload'], function () {
    var $ = layui.$,
        form = layui.form,
        layer = layui.layer,
        laytpl = layui.laytpl,
        upload = layui.upload,
        laydate = layui.laydate;

    // 初始化日期选择器
    laydate.render({
        elem: '#startDate', //指定元素
        range: false //开启日期范围，默认使用“_”分割
    });
    // 初始化日期选择器
    laydate.render({
        elem: '#endDate', //指定元素
        range: false //开启日期范围，默认使用“_”分割
    });

    form.on('submit(save)', function (data) {
        console.log(JSON.stringify(data.field));
        // data.field.id = $("#id").val();
        // alert(JSON.stringify(data.field));
        var ajaxReturnData;
        $.ajax({
            url: PageContext.getUrl('/wdmb/rwfj'),
            contentType: "application/json",
            type: 'post',
            async: false,
            data: JSON.stringify(data.field),
            success: function (data) {
                ajaxReturnData = data;
            }
        });
        //结果回应
        if (ajaxReturnData.flag == 'true') {
            top.layer.msg('保存成功', {icon: 1});
            // 保存成功后禁用掉保存按钮
            // $('#saveButton').addClass('layui-btn-disabled').attr('disabled', "true");
            //先得到当前iframe层的索引
            //var index = parent.layer.getFrameIndex(window.name);
            //parent.layer.close(index); //再执行关闭
            //刷新父页面
            // parent.location.reload();
            window.location.href = PageContext.getUrl("/wdmb/list");
        } else {
            top.layer.msg(ajaxReturnData.msg, {icon: 5});
        }
        return false;
    });

    //多文件多次上传-layui不支持一次传输文件
    //定义一个字符串存取attachFile异步请求数据
    var map = {};
    var fileName = null;
    //多文件上传
    upload.render({
        elem: '#test2'
        , url: PageContext.getUrl('/fuJian/upload/')
        , accept: 'file'
        , exts: 'jpg|jpeg|gif|png|doc|docx|pdf|xlsx|xls|pptx|ppt|rar|zip'
        // , multiple: true
        , before: function (obj) {
            //预读本地文件示例，不支持ie8
            obj.preview(function (index, file, result) {
                // fileName=file.name;
                // map[file.name]='';
            });
        }
        , done: function (res) {
            if (res.code == '0') {
                // var i=0;
                // for(var prop in map){
                //   if(map.hasOwnProperty(prop)){
                map[res.oldFileName] = eval("(" + res.attachFile + ")")[0].path;
                //     i++;
                //   }
                // }
                var ht = $('#demo2').html();
                $('#demo2').html(ht + '<a href="' + eval("(" + res.attachFile + ")")[0].path + '" target="_blank"><i class="layui-icon layui-icon-close"></i>' + res.oldFileName + '</a>');
                //将获取的文件放入map中
                var A = $("#attachFile").val();
                if (A == null || A == undefined || A == "") {
                    $("#attachFile").val(JSON.stringify(map));
                } else {
                    // $("#attachFile").val(JSON.stringify($.extend(map, eval("(" + $("#attachFile").val() + ")"))));
                    $("#attachFile").val(JSON.stringify(map));
                }
                layer.msg("文件" + res.tmpFileName + "上传成功");
                //上传完毕
            } else if (res.code == '1') {
                layer.msg("文件" + res.tmpFileName + "不能为空");
                $('#demo2').html(" ");
            } else if (res.code = 'false') {
                layer.msg(res.message);
                $('#demo2').html(" ");
            } else if (res.code = 'lose') {
                layer.msg("文件" + res.tmpFileName + "上传失败");
                $('#demo2').html(" ");
            } else if (res.code = 'big') {
                layer.msg("文件" + res.tmpFileName + "大小不能超过20M");
                $('#demo2').html(" ");
            } else {
                layer.msg("文件上传时发生错误,请稍后重试");
                $('#demo2').html(" ");
            }
        }
    });

    //点击事件
    $(document).on('click', '.pdfList i', function () {
        delete(map[$(this)[0].parentNode.text]);
        $("#attachFile").val(JSON.stringify(map));
        $(this).parent('a').remove();
    });

    //按钮事件监听
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        add: function () {
            window.location.href = PageContext.getUrl("/wdmb/toAdd" + "?id=" + $("#id").val() + "&xmId=" + $("#xmId").val() + "&xmjd=" + $("#xmjd").val());
        },
        goBack: function () {
            window.location.href = PageContext.getUrl("/xmxxgl/get/xmgl/" + $("#xmId").val() + "?xmjd=" + $("#xmjd").val());
        }
    };


    window.deleteData = function (obj) {
        layer.confirm('真的要删除吗？', function (index) {
            window.location.href = PageContext.getUrl("/wdmb/delete" + "?id=" + $("#id").val());
            layer.close(index);
        });
    };


});