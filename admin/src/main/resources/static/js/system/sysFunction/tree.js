//一般直接写在一个js文件中
layui.use(['layer', 'form', 'table'], function () {
    var layer = layui.layer
        , form = layui.form
        , $ = layui.$
        , laytpl = layui.laytpl
        , table = layui.table;

    pageInit();


});


function pageInit() {
    // zTree 的参数配置
    var setting = {
        view: {
            addHoverDom: addHoverDom,//添加鼠标悬浮事件
            removeHoverDom: removeHoverDom,//鼠标悬浮接触事件
            selectedMulti: false
        },
        edit: {
            enable: true,//允许编辑
            renameTitle: "修改",
            showRenameBtn: true,
            editNameSelectAll: true,
            removeTitle: "删除",
            showRemoveBtn: showRemoveBtn//显示删除按钮
        },
        callback: {
            beforeEditName: beforeEditName,
            beforeRemove: beforeRemove,//删除前触发事件
            onRemove: null//删除触发
        }
    };

    //新增
    function addHoverDom(treeId, treeNode) {
        //3级菜单没有新增按钮
        if (treeNode.functionType == 'subModule') {
            return false;
        } else {
            var sObj = $("#" + treeNode.tId + "_span");
            if (treeNode.editNameFlag || $("#addBtn_" + treeNode.tId).length > 0) return;
            var addStr = "<span class='button add' id='addBtn_" + treeNode.tId
                + "' title='新增' onfocus='this.blur();'></span>";
            sObj.after(addStr);
            var btn = $("#addBtn_" + treeNode.tId);
            //点击加号时触发
            if (btn) btn.bind("click", function () {
                var pId = treeNode.id;
                //开启表单弹层
                var addLayerIndex = layer.open({
                    title: '新建菜单节点',
                    type: 2,
                    skin: 'layui-layer-molv',
                    offset: ['85px', '530px'],
                    area: ['540px', '620px'],
                    content: PageContext.getUrl("/sysfunction/toadd"),
                    success: function (layero, index) {
                        var body = layui.layer.getChildFrame('body', index);
                        if (pId) {
                            body.find("#parentCodeLabel").text(pId);
                            layui.form.render();
                        }
                    }
                });
                return false;
            });

        }
    };

    //移除鼠标悬浮时临时增加的按钮
    function removeHoverDom(treeId, treeNode) {
        $("#addBtn_" + treeNode.tId).unbind().remove();
    };


    function beforeEditName(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("sysFunctionTree");
        zTree.selectNode(treeNode);
        //根据id获取菜单节点对象的数据并填充进编辑的表单中
        $.ajax({
            type: "POST",
            url: PageContext.getUrl("/sysfunction/get"),
            data: {code: treeNode.id},
            async: false,
            error: function () {
                layer.alert("系统繁忙，请稍后重试");
            },
            success: function (data) {
                if (data.flag == 'true') {
                    var sysFunction = data.sysFunction;
                    layer.open({
                        title: '新建菜单节点',
                        type: 2,
                        skin: 'layui-layer-molv',
                        offset: ['85px', '530px'],
                        area: ['540px', '620px'],
                        content: PageContext.getUrl("/sysfunction/toadd"),
                        success: function (layero, index) {
                            var body = layui.layer.getChildFrame('body', index);
                            body.find("#parentCodeLabel").text(sysFunction.parentCode);
                            body.find("#idLabel").text(sysFunction.id);
                            body.find("#icon").val(sysFunction.icon);
                            body.find("#code").val(sysFunction.code);
                            body.find("#name").val(sysFunction.name);
                            body.find("#url").val(sysFunction.url);
                            //有效性赋值
                            body.find("input[name=status]").each(function (i, item) {
                                if ($(item).val() == data.status) {
                                    $(item).prop('checked', true);
                                }
                            });
                            //节点类型
                            body.find("#functionType").val(sysFunction.functionType);
                            body.find("#seqnum").val(sysFunction.seqnum);
                        }
                    });
                    return false;
                } else {
                    layer.msg(data.msg, {icon: 5});
                    return false;
                }
            }
        });
        return false;
    }

    //哪些节点会显示删除按钮，哪些节点不会显示
    function showRemoveBtn(treeId, treeNode) {
        return treeNode.name != '菜单管理'
            && treeNode.name != '角色管理'
            && treeNode.name != '用户管理'
            && treeNode.pId != '-1';
    }

    //删除之前提示确认
    function beforeRemove(treeId, treeNode) {
        var zTree = $.fn.zTree.getZTreeObj("sysFunctionTree");
        zTree.selectNode(treeNode);
        layer.confirm('确认删除 节点 -- ' + treeNode.name + '及其关联数据 吗？', {
            btn: ['确定', '取消']
        }, function () {
            $.ajax({
                type: "POST",
                url: PageContext.getUrl("/sysfunction//delete"),
                data: {code: treeNode.id},
                async: false,
                error: function () {
                    layer.alert("系统繁忙，请稍后重试");
                },
                success: function (data) {
                    if (data.flag == 'true') {
                        layer.msg(data.msg, {icon: 1, time: 1000}, function () {
                            window.location.reload();
                        });
                    } else {
                        layer.msg(data.msg, {icon: 5});
                        return false;
                    }
                }
            });
            return false;
        });
        return false;
    }


    //初始化系统功能树
    $.ajax({
        type: "POST",
        url: PageContext.getUrl('/sysfunction/load/sysfunction?parentCode=-1'),
        async: false,
        dataType: 'json',
        timeout: 1000,
        cache: false,
        error: function () {
            layer.alert("系统繁忙，请稍后重试");
        },
        success: function (data) {
            var zNodes = data;
            $.fn.zTree.init($("#sysFunctionTree"), setting, zNodes);
        }
    });

}