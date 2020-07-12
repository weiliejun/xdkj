layui.use(['layer', 'laydate', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table,
        laydate = layui.laydate;

    // 初始化日期选择器
    laydate.render({
        elem: '#rangeTime1', //指定元素
        range: true //开启日期范围，默认使用“_”分割
    });

    var toolbar = '#toolbarDemo';
    var type = $("#type").val();
    var cxmk = $("#cxmk").val();

    var cols = [[
        {type: type},
        {
            field: 'name',
            title: '姓名',
            align: 'center',
            sort: true
        }, {
            field: 'phone',
            title: '手机号码',
            align: 'center',
            sort: true
        }, {
            field: 'gsyx',
            title: '公司邮箱',
            align: 'center',
            width: '20%',
            sort: true
        }, {
            field: 'gryx',
            title: '个人邮箱',
            align: 'center',
            width: '20%',
            sort: true
        }, {
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#dlsInfoBar',
            width: '20%',
            sort: true
        }
    ]];

    // 表格渲染
    var initTable = Common.initTable('#dlsInfoTables', '/dlsInfo/selectList', cols, table);

    //监听列工具条
    table.on('tool(dlsInfoTables)', function (obj) {
        var data = obj.data;
        //修改
        if (obj.event === 'update') {
            window.location.href = PageContext.getUrl("/dlsInfo/get/update/" + data.id);

        } else if (obj.event === 'view') {
            window.location.href = PageContext.getUrl("/dlsInfo/get/view/" + data.id);
        }
    });
    //头工具栏事件
    table.on('toolbar(dlsInfoTables)', function (obj) {
        var checkStatus = table.checkStatus(obj.config.id); //获取选中行状态
        switch (obj.event) {
            case 'getCheckData':
                var data = checkStatus.data;  //获取选中行数据
                if (data == null || data == undefined || data == "") {
                    layer.alert("请选择一条数据！");
                    return false;
                }
                // layer.alert(JSON.stringify(data));

                var json = eval('(' + JSON.stringify(data) + ')');
                // var  json= $.parseJSON(JSON.stringify(data));
                console.log("type------" + type);
                console.log("cxmk------" + cxmk);

                if (cxmk == "selectXmfzr") {
                    //项目负责人
                    window.parent.selectXmfzrCallback(json[0]);
                } else if (cxmk == "selectXmjbr") {
                    //项目经办人
                    window.parent.selectXmjbrCallback(json[0]);
                } else if (cxmk == "selectFwfzr") {
                    //法务负责人
                    window.parent.selectFwfzrCallback(json[0]);
                } else if (cxmk == "selectCwfzr") {
                    //财务负责人
                    window.parent.selectCwfzrCallback(json[0]);
                } else if (cxmk == "selectSpr") {
                    //审批人
                    window.parent.selectSprCallback(json);
                } else if (cxmk == "selectXmqtcy") {
                    //项目其他成员
                    // layer.alert("selectXmqtcy");
                    window.parent.selectXmqtcyCallback(json);
                } else if (cxmk == "selectJsxmSpr") {
                    //结束项目审批人
                    window.parent.selectJsxmSprCallback(json);
                } else if (cxmk == "selectFzr") {
                    //任务负责人
                    window.parent.selectFzrCallback(json[0]);
                } else if (cxmk == "selectFhr") {
                    //任务复核人
                    window.parent.selectFhrCallback(json);
                } else if (cxmk == "selectZhr") {
                    //任务知会人
                    window.parent.selectZhrCallback(json);
                }
                //先得到当前iframe层的索引
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index); //再执行关闭
                break;
            case 'cancel':
                //点击取消关闭窗口
                var index = parent.layer.getFrameIndex(window.name);
                parent.layer.close(index);

        }

    });

    //点击table行选中复选框  开始////
    $(document).on("click", ".layui-table-body table.layui-table tbody tr", function () {
        var obj = event ? event.target : event.srcElement;
        var tag = obj.tagName;
        var checkbox = $(this).find("td div.laytable-cell-checkbox div.layui-form-checkbox I");
        if (checkbox.length != 0) {
            if (tag == 'DIV') {
                checkbox.click();
            }
        }

    });

    $(document).on("click", "td div.laytable-cell-checkbox div.layui-form-checkbox", function (e) {
        e.stopPropagation();
    });

    $(document).on("click", ".layui-table-body table.layui-table tbody tr", function () {
        var obj = event ? event.target : event.srcElement;
        var tag = obj.tagName;
        var radio = $(this).find("td div.laytable-cell-radio div.layui-form-radio I");
        if (radio.length != 0) {
            if (tag == 'DIV') {
                radio.click();
            }
        }

    });

    $(document).on("click", "td div.laytable-cell-radio div.layui-form-radio", function (e) {
        e.stopPropagation();
    });
    //点击table行选中复选框 结束////
    //按钮事件监听
    $('.layui-btn').on('click', function () {
        var type = $(this).data('type');
        active[type] ? active[type].call(this) : '';
    });

    //按钮事件定义
    var active = {
        search: function () {
            Common.searchTable('searchForm', initTable);
        },
        searchFormClear: function () {
            Common.searchTableClear('searchForm');
        }
    };

    //打开新窗口
    function addTab(_this) {
        tab.tabAdd(_this);
    }
});

