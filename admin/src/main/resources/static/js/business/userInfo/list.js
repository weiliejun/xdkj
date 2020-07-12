layui.use(['layer', 'form', 'table'], function () {
    var $ = layui.$,
        layer = layui.layer,
        form = layui.form,
        table = layui.table;

    var cols = [[
        {
            field: 'userName',
            width: 220,
            title: '姓名',
            align: 'center',
            sort: true
        }, {
            field: 'nickName',
            width: 220,
            title: '用户名',
            align: 'center'
        }, {
            field: 'mobile',
            width: 220,
            title: '手机号码',
            align: 'center'
        }/*,{
            title: '常用操作',
            align: 'center',
            fixed: "right",
            toolbar: '#userInfoBar'
        }*/
    ]];

    // 表格渲染
    var initTable = Common.initTable('#userInfoTables', '/userInfo/list', cols, table);

    //监听工具条
    table.on('tool(userInfoTables)', function (obj) {
        var data = obj.data;
        // 设为成功
        if (obj.event === 'editStatus') {
            var reqData = {userTopupId: data.id};
            Common.confirmFrame('/userInfo/editStatus', '确定要修改充值状态么？', reqData, 'userInfoTables', table);
        }
    });

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
});