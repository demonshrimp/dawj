/**
 * Created by ksy on 2016-2-24.
 */
(function ($, AdminLTE, App) {

    loginCheck();
    var menuTree;
    /**
     * List of all the available skins
     *
     * @type Array
     */
    var my_skins = [
        "skin-blue",
        "skin-black",
        "skin-red",
        "skin-yellow",
        "skin-purple",
        "skin-green",
        "skin-blue-light",
        "skin-black-light",
        "skin-red-light",
        "skin-yellow-light",
        "skin-purple-light",
        "skin-green-light"
    ];

    //Create the new tab
    var tab_pane = $("<div />", {
        "id": "control-sidebar-theme-demo-options-tab",
        "class": "tab-pane active"
    });

    //Create the tab button
    var tab_button = $("<li />", {"class": "active"})
        .html("<a href='#control-sidebar-theme-demo-options-tab' data-toggle='tab'>"
            + "<i class='fa fa-wrench'></i>"
            + "</a>");

    //Add the tab button to the right sidebar tabs
    $("[href='#control-sidebar-home-tab']")
        .parent()
        .before(tab_button);

    //Create the menu
    var demo_settings = $("<div />");

    //Layout options
    demo_settings.append(
        "<h4 class='control-sidebar-heading'>"
        + "Layout Options"
        + "</h4>"
            //Fixed layout
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-layout='fixed' class='pull-right'/> "
        + "Fixed layout"
        + "</label>"
        + "<p>Activate the fixed layout. You can't use fixed and boxed layouts together</p>"
        + "</div>"
            //Boxed layout
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-layout='layout-boxed'class='pull-right'/> "
        + "Boxed Layout"
        + "</label>"
        + "<p>Activate the boxed layout</p>"
        + "</div>"
            //Sidebar Toggle
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-layout='sidebar-collapse' class='pull-right'/> "
        + "Toggle Sidebar"
        + "</label>"
        + "<p>Toggle the left sidebar's state (open or collapse)</p>"
        + "</div>"
            //Sidebar mini expand on hover toggle
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-enable='expandOnHover' class='pull-right'/> "
        + "Sidebar Expand on Hover"
        + "</label>"
        + "<p>Let the sidebar mini expand on hover</p>"
        + "</div>"
            //Control Sidebar Toggle
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-controlsidebar='control-sidebar-open' class='pull-right'/> "
        + "Toggle Right Sidebar Slide"
        + "</label>"
        + "<p>Toggle between slide over content and push content effects</p>"
        + "</div>"
            //Control Sidebar Skin Toggle
        + "<div class='form-group'>"
        + "<label class='control-sidebar-subheading'>"
        + "<input type='checkbox' data-sidebarskin='toggle' class='pull-right'/> "
        + "Toggle Right Sidebar Skin"
        + "</label>"
        + "<p>Toggle between dark and light skins for the right sidebar</p>"
        + "</div>"
    );
    var skins_list = $("<ul />", {"class": 'list-unstyled clearfix'});

    //Dark sidebar skins
    var skin_blue =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-blue' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px; background: #367fa9;'></span><span class='bg-light-blue' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Blue</p>");
    skins_list.append(skin_blue);
    var skin_black =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-black' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div style='box-shadow: 0 0 2px rgba(0,0,0,0.1)' class='clearfix'><span style='display:block; width: 20%; float: left; height: 7px; background: #fefefe;'></span><span style='display:block; width: 80%; float: left; height: 7px; background: #fefefe;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Black</p>");
    skins_list.append(skin_black);
    var skin_purple =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-purple' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-purple-active'></span><span class='bg-purple' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Purple</p>");
    skins_list.append(skin_purple);
    var skin_green =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-green' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-green-active'></span><span class='bg-green' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Green</p>");
    skins_list.append(skin_green);
    var skin_red =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-red' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-red-active'></span><span class='bg-red' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Red</p>");
    skins_list.append(skin_red);
    var skin_yellow =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-yellow' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-yellow-active'></span><span class='bg-yellow' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #222d32;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin'>Yellow</p>");
    skins_list.append(skin_yellow);

    //Light sidebar skins
    var skin_blue_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-blue-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px; background: #367fa9;'></span><span class='bg-light-blue' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px'>Blue Light</p>");
    skins_list.append(skin_blue_light);
    var skin_black_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-black-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div style='box-shadow: 0 0 2px rgba(0,0,0,0.1)' class='clearfix'><span style='display:block; width: 20%; float: left; height: 7px; background: #fefefe;'></span><span style='display:block; width: 80%; float: left; height: 7px; background: #fefefe;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px'>Black Light</p>");
    skins_list.append(skin_black_light);
    var skin_purple_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-purple-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-purple-active'></span><span class='bg-purple' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px'>Purple Light</p>");
    skins_list.append(skin_purple_light);
    var skin_green_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-green-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-green-active'></span><span class='bg-green' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px'>Green Light</p>");
    skins_list.append(skin_green_light);
    var skin_red_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-red-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-red-active'></span><span class='bg-red' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px'>Red Light</p>");
    skins_list.append(skin_red_light);
    var skin_yellow_light =
        $("<li />", {style: "float:left; width: 33.33333%; padding: 5px;"})
            .append("<a href='javascript:void(0);' data-skin='skin-yellow-light' style='display: block; box-shadow: 0 0 3px rgba(0,0,0,0.4)' class='clearfix full-opacity-hover'>"
                + "<div><span style='display:block; width: 20%; float: left; height: 7px;' class='bg-yellow-active'></span><span class='bg-yellow' style='display:block; width: 80%; float: left; height: 7px;'></span></div>"
                + "<div><span style='display:block; width: 20%; float: left; height: 20px; background: #f9fafc;'></span><span style='display:block; width: 80%; float: left; height: 20px; background: #f4f5f7;'></span></div>"
                + "</a>"
                + "<p class='text-center no-margin' style='font-size: 12px;'>Yellow Light</p>");
    skins_list.append(skin_yellow_light);

    demo_settings.append("<h4 class='control-sidebar-heading'>Skins</h4>");
    demo_settings.append(skins_list);

    tab_pane.append(demo_settings);
    $("#control-sidebar-home-tab").after(tab_pane);

    setup();

    /**
     * Toggles layout classes
     *
     * @param String cls the layout class to toggle
     * @returns void
     */
    function change_layout(cls) {
        $("body").toggleClass(cls);
        AdminLTE.layout.fixSidebar();
        //Fix the problem with right sidebar and layout boxed
        if (cls == "layout-boxed")
            AdminLTE.controlSidebar._fix($(".control-sidebar-bg"));
        if ($('body').hasClass('fixed') && cls == 'fixed') {
            AdminLTE.pushMenu.expandOnHover();
            AdminLTE.layout.activate();
        }
        AdminLTE.controlSidebar._fix($(".control-sidebar-bg"));
        AdminLTE.controlSidebar._fix($(".control-sidebar"));
    }

    /**
     * Replaces the old skin with the new skin
     * @param String cls the new skin class
     * @returns Boolean false to prevent link's default action
     */
    function change_skin(cls) {
        $.each(my_skins, function (i) {
            $("body").removeClass(my_skins[i]);
        });

        $("body").addClass(cls);
        store('skin', cls);
        return false;
    }

    /**
     * Store a new settings in the browser
     *
     * @param String name Name of the setting
     * @param String val Value of the setting
     * @returns void
     */
    function store(name, val) {
        if (typeof (Storage) !== "undefined") {
            localStorage.setItem(name, val);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }

    /**
     * Get a prestored setting
     *
     * @param String name Name of of the setting
     * @returns String The value of the setting | null
     */
    function get(name) {
        if (typeof (Storage) !== "undefined") {
            return localStorage.getItem(name);
        } else {
            window.alert('Please use a modern browser to properly view this template!');
        }
    }

    /**
     * Retrieve default settings and apply them to the template
     *
     * @returns void
     */
    function setup() {
        initPageLoader();
        setupGlobalAjax();
        var tmp = get('skin');
        if (tmp && $.inArray(tmp, my_skins))
            change_skin(tmp);

        //Add the change skin listener
        $("[data-skin]").on('click', function (e) {
            e.preventDefault();
            change_skin($(this).data('skin'));
        });

        //Add the layout manager
        $("[data-layout]").on('click', function () {
            change_layout($(this).data('layout'));
        });

        $("[data-controlsidebar]").on('click', function () {
            change_layout($(this).data('controlsidebar'));
            var slide = !AdminLTE.options.controlSidebarOptions.slide;
            AdminLTE.options.controlSidebarOptions.slide = slide;
            if (!slide)
                $('.control-sidebar').removeClass('control-sidebar-open');
        });

        $("[data-sidebarskin='toggle']").on('click', function () {
            var sidebar = $(".control-sidebar");
            if (sidebar.hasClass("control-sidebar-dark")) {
                sidebar.removeClass("control-sidebar-dark")
                sidebar.addClass("control-sidebar-light")
            } else {
                sidebar.removeClass("control-sidebar-light")
                sidebar.addClass("control-sidebar-dark")
            }
        });

        $("[data-enable='expandOnHover']").on('click', function () {
            $(this).attr('disabled', true);
            AdminLTE.pushMenu.expandOnHover();
            if (!$('body').hasClass('sidebar-collapse'))
                $("[data-layout='sidebar-collapse']").click();
        });

        // Reset options
        if ($('body').hasClass('fixed')) {
            $("[data-layout='fixed']").attr('checked', 'checked');
        }
        if ($('body').hasClass('layout-boxed')) {
            $("[data-layout='layout-boxed']").attr('checked', 'checked');
        }
        if ($('body').hasClass('sidebar-collapse')) {
            $("[data-layout='sidebar-collapse']").attr('checked', 'checked');
        }
        initCommonEvents();
    }

    function loadSidebarMenus() {
        var url = window.location.toString();
        jQuery.get("data/menus.json", function (datas) {
            menuTree = datas;
            var sidebarMenu = $("#sidebarMenu");
            App.Utils.ArrayUtil.traverseTree(datas, function (node, level) {
                var menu = node;
                if (menu.no.indexOf(2) == 0 && getSiteName() != 'root') {
                    return;
                }
                if (level == 0) {
                    sidebarMenu.append(buildMenuHeader(menu));
                } else if (level == 1) {
                    sidebarMenu.append(buildMenu(menu));
                } else {
                    var ul = findParentMenuUl(menu);
                    ul.append(buildMenu(menu));
                }
            });
        });
    }

    function buildMenuHeader(menu) {
        return '<li id="sidebar-menu-' + menu.no + '" class="header">' + menu.name + '</li>';
    }

    function buildMenu(menu) {
        var li = $('<li id="sidebar-menu-' + menu.no + '" class="treeview">');
        li.append(buildMenuBtn(menu));
        return li;
    }

    function findParentMenuUl(menu) {
        var parentNo = menu.no.substr(0, menu.no.length - 1);
        var parentLi = $("#sidebar-menu-" + parentNo);
        var ul = parentLi.find("ul.treeview-menu");
        if (ul.length > 0) {
            ul = $(ul[0]);
        } else {
            ul = $('<ul class="treeview-menu">');
            parentLi.append(ul);
            var a = $(parentLi[0].children[0]);
            a.append('<i class="fa fa-angle-left pull-right"></i>');
        }
        return ul;
    }

    function buildMenuBtn(menu) {
        var a = $('<a>');
        a.data(menu);
        var url = menu.url ? menu.url : "";
        a.attr("href", "#" + url);
        menu.icon = menu.icon ? menu.icon : "fa-circle-o";
        a.append('<i class="fa ' + menu.icon + '"></i>');
        a.append('<span>' + menu.name + '</span>');
        return a;
    }


    var map = [{url: 'pages/counseling/counseling-type.html', no: '121'},
        {url: 'pages/counseling/counselor.html', no: '122'},
        {url: 'pages/counseling/counseling-article.html', no: '123'},
        {url: 'pages/sys/site.html', no: '21'}
    ];

    function initPageLoader() {
        var breadcrumb = $('#breadcrumb');
        if ($(window).hashchange) {
            $(window).hashchange(function () {
                var hash = location.hash;
                var page = hash.substr(1);
                if (page) {
                    $("#wrapperContent").load(page, function (response, status, xhr) {
                        breadcrumb.empty();
                        breadcrumb.append('<li><a href="#home/dashbord.html"><i class="fa fa-dashboard"></i> Home</a></li>');
                        if (xhr.status == 404) {
                            $("#wrapperContent").load('pages/home/404.html');
                            breadcrumb.append('<li><i class="fa fa-ban-circle"></i> 404</li>');
                        }
                        var subPageMenuNo;
                        for (var i = 0; i < map.length; i++) {
                            if (page.indexOf(map[i].url) >= 0) {
                                subPageMenuNo = map[i].no;
                            }
                        }
                        if (subPageMenuNo) {
                            buildFormMenuTree(menuTree, subPageMenuNo);
                        } else {
                            App.Utils.ArrayUtil.traverseTree(menuTree, function (node, level) {
                                if (node.url == page) {
                                    buildFormMenuTree(menuTree, node.no);
                                    return false;
                                }
                            });
                        }
                    });
                }
            });
            var indexUrl = "pages/home/dashboard.html";
            if (window.location.toString().indexOf("#") < 0) {
                window.location.href = "#" + indexUrl;
            } else {
                $(window).hashchange();
            }
            $(document).ajaxStart(function () {
                Pace.restart();
            });
            $("#sidebarMenu").on("click", "a", function () {
                var url = $(this).attr("href").substr(1);
                if (url == "") {
                    return;
                }
                $(".sidebar-menu .active").removeClass("active");
                $(this).parents("li").addClass("active");
            });
            loadSidebarMenus();
        }
    }

    function buildFormMenuTree(menuTree, nodeNo) {
        var menu = {children: menuTree};
        for (var i = 1; i <= nodeNo.length; i++) {
            var no = parseInt(nodeNo.substr(i - 1, 1)) - 1;
            menu = menu.children[no];
            if (i > 1) {
                $('#breadcrumb').append('<li>' + menu.name + '</li>');
            }
        }
    }

    function setupGlobalAjax() {
        var site = App.getCurrentLoginSite();
        $.ajaxSetup({
            headers: {
                '_stoken': App.getCurrentLoginSite().token,
                '_sname': getSiteName()
            },
            beforeSend: function (request, settings) {
                //request.setRequestHeader("dataType","abc");
                /*if(settings.contentType == "application/json"){
                 var obj = JSON.parse(settings.data);
                 obj._stoken = site.token;
                 settings.data = JSON.stringify(obj);
                 }else{
                 settings.data._stoken = site.token;
                 }*/
                request.done(function (result) {
                    if (result.header) {
                        if (result.header.errorCode == "900" || result.header.errorCode == "901") {
                            alert(result.header.message);
                            toLoginPage();
                        }
                    }
                })
            }
        });
    }

    function initCommonEvents() {
        /*$(document).on('click','.img-preview',function(){
         $(this).popover({
         html: true,
         template:'<div class="popover" role="tooltip"><div class="arrow"></div><h3 class="popover-title">图片预览</h3><div class="popover-content"><img src="'+$(this).attr('src')+'"></div></div>'
         });
         $(this).popover('show');
         })*/
    }

    function loginCheck() {
        var site = App.getCurrentLoginSite();
        if (!site) {
            if (window.location.href.toString().indexOf('/login.html') < 0) {
                toLoginPage();
            }
        } else {
            $('.user-name').text(site.name);
            $('.user-detail').text(site.city);
            $('.user-logoff').click(function () {
                window.sessionStorage.removeItem(App.Constants.KEY_CURRENT_USER);
                toLoginPage();
            });
        }
    }

    function toLoginPage() {
        var site = getSiteName();
        window.location.href = App.Utils.UrlUtil.getProjectRoot() + '/login.html?site=' + site;
    }

    function getSiteName() {
        var site = App.Utils.UrlUtil.getUrlParameter('site');
        if (!site) {
            site = 'root';
        }
        return site;
    }
})(jQuery, $.AdminLTE, App);