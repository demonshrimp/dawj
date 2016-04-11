/**
 * AdminLTE Demo Menu
 * ------------------
 * You should not use this file in production.
 * This file is for demo purposes only.
 */
var App = {
    Constants: {},
    Utils: {}
};
(function ($, AdminLTE, App) {
    "use strict";

    App.Constants.API_BASE = "http://localhost:8080/dawj-server/api/admin";

    App.Constants.KEY_CURRENT_USER = "_ASDdawjus";

    App.loadPage = function (url) {
        window.location.href = "#" + url;
    };

    App.getCurrentLoginSite = function () {
        var s = window.sessionStorage.getItem(App.Constants.KEY_CURRENT_USER);
        if (s) {
            return JSON.parse(s);
        }
    };

    App.setCurrentLoginSite = function (user) {
        window.sessionStorage.setItem(App.Constants.KEY_CURRENT_USER, JSON.stringify(user));
    };

    App.Utils.ArrayUtil = {
        /**
         *
         * @param {Array|Object} tree
         * @param {Function} handle function(child, level){}
         * @param {int} level
         * @param {Object} params
         * default
         * {
		 * 	childrenKey: 'children'
		 * }
         *
         */
        traverseTree: function (tree, handle, level, params) {
            level = level ? level : 0;
            params = $.extend({
                childrenKey: 'children'
            }, params);
            if (!tree instanceof Array) {
                tree = [tree];
            }
            for (var i = 0; tree && i < tree.length; i++) {
                handle(tree[i], level);
                this.traverseTree(tree[i][params.childrenKey], handle, (level + 1), params);
            }
        }
    }

    App.Utils.ObjectUtil = {
        getValue: function (data, key) {
            var keys = key.split('.');
            var k = keys[0];
            if (keys.length > 1) {
                var ksub = key.substr(k.length + 1);
                var isArray = false
                if (k.indexOf('[]') > 0) {
                    k = k.substr(0, k.length - 2);
                    isArray = true;
                }
                if (isArray) {
                    var array = [];
                    for (var j = 0; j < data[k].length; j++) {
                        array.push(this.getValue(data[k][j], ksub));
                    }
                    return array;
                } else {
                    return this.getValue(data[k], ksub);
                }
            } else {
                return data[k];
            }
        },
        setValue: function (data, key, value) {
            var keys = key.split('.');
            var k = keys[0];
            if (keys.length > 1) {
                var ksub = key.substr(k.length + 1);
                var isArray = false;
                if (k.indexOf('[]') > 0) {
                    k = k.substr(0, k.length - 2);
                    isArray = true;
                }
                var obj = {};
                this.setValue(obj, ksub, value);
                if (isArray) {
                    if (!data[k]) {
                        data[k] = [];
                    }
                    data[k].push(obj);
                } else {
                    data[k] = obj;
                }
            } else {
                data[k] = value;
            }
        }
    }

    App.Utils.FormUtil = {
        toJson: function (form) {
            if (!(form instanceof jQuery)) {
                form = $(form);
            }
            var serializeObj = {};
            var array = form.serializeArray();
            $(array).each(function () {
                App.Utils.ObjectUtil.setValue(serializeObj, this.name, this.value);
                /*if (serializeObj[this.name]) {
                 if ($.isArray(serializeObj[this.name])) {
                 serializeObj[this.name].push(this.value);
                 } else {
                 serializeObj[this.name] = [serializeObj[this.name], this.value];
                 }
                 } else {
                 serializeObj[this.name] = this.value;
                 }*/
            });
            return serializeObj;
        },
        setData: function (form, data) {
            $(form).find('[name]').each(function () {
                var key = $(this).attr('name');
                var value = App.Utils.ObjectUtil.getValue(data, key);
                if (value && value.toString && !(value instanceof Array)) {
                    value = value.toString();
                }
                $(this).val(value);
            });
        }
    }


    App.Utils.DataTablesHelper = {
        buildOperationButtons: function (buttons, data) {
            var btns = "";
            for (var i = 0; i < buttons.length; i++) {
                var button = buttons[i];
                var a = $('<a  href="javascript:void(0);" class="btn-operation"></a>');
                for (var p in data) {
                    a.attr("data-" + p, data[p]);
                }
                a.attr("data-operation", button.operation);
                a.text(button.text);
                btns += a[0].outerHTML + " / ";
            }
            if (btns.length > 3) {
                btns = btns.substr(0, btns.length - 3);
            }
            return btns;
        },
        onOperationButtonClick: function (tableSelector, handle) {
            $(tableSelector).on("click", ".btn-operation", function () {
                var data = $(this).data();
                handle.apply(this, [data]);
            });
        },
        language: function (language) {
            return $.extend(this.defaultLanguage, language);
        },
        defaultLanguage: {
            "sProcessing": "处理中...",
            "sLengthMenu": "显示 _MENU_ 项结果",
            "sZeroRecords": "没有匹配结果",
            "sInfo": "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
            "sInfoEmpty": "显示第 0 至 0 项结果，共 0 项",
            "sInfoFiltered": "(由 _MAX_ 项结果过滤)",
            "sInfoPostFix": "",
            "sSearch": "搜索:",
            "sUrl": "",
            "sEmptyTable": "表中数据为空",
            "sLoadingRecords": "载入中...",
            "sInfoThousands": ",",
            "oPaginate": {
                "sFirst": "首页",
                "sPrevious": "上页",
                "sNext": "下页",
                "sLast": "末页"
            },
            "oAria": {
                "sSortAscending": ": 以升序排列此列",
                "sSortDescending": ": 以降序排列此列"
            }
        },
        pipeline: function () {
            //
            // Pipelining function for DataTables. To be used to the `ajax` option of DataTables
            //
            $.fn.dataTable.pipeline = function (opts) {
                // Configuration options
                var conf = $.extend({
                    pages: 0,     // number of pages to cache
                    url: '',      // script url
                    data: null,   // function or object with parameters to send to the server
                                  // matching how `ajax.data` works in DataTables
                    method: 'GET' // Ajax HTTP method
                }, opts);

                // Private variables for storing the cache
                var cacheLower = -1;
                var cacheUpper = null;
                var cacheLastRequest = null;
                var cacheLastJson = null;

                return function (request, drawCallback, settings) {
                    var ajax = false;
                    var requestStart = request.start;
                    var drawStart = request.start;
                    var requestLength = request.length;
                    var requestEnd = requestStart + requestLength;

                    if (settings.clearCache) {
                        // API requested that the cache be cleared
                        ajax = true;
                        settings.clearCache = false;
                    }
                    else if (cacheLower < 0 || requestStart < cacheLower || requestEnd > cacheUpper) {
                        // outside cached data - need to make a request
                        ajax = true;
                    }
                    else if (JSON.stringify(request.order) !== JSON.stringify(cacheLastRequest.order) ||
                        JSON.stringify(request.columns) !== JSON.stringify(cacheLastRequest.columns) ||
                        JSON.stringify(request.search) !== JSON.stringify(cacheLastRequest.search)
                    ) {
                        // properties changed (ordering, columns, searching)
                        ajax = true;
                    }

                    // Store the request for checking next time around
                    cacheLastRequest = $.extend(true, {}, request);

                    if (ajax) {
                        // Need data from the server
                        if (requestStart < cacheLower) {
                            requestStart = requestStart - (requestLength * (conf.pages - 1));

                            if (requestStart < 0) {
                                requestStart = 0;
                            }
                        }

                        cacheLower = requestStart;
                        cacheUpper = requestStart + (requestLength * conf.pages);

                        request.start = requestStart;
                        request.length = requestLength * conf.pages;

                        // Provide the same `data` options as DataTables.
                        if ($.isFunction(conf.data)) {
                            // As a function it is executed with the data object as an arg
                            // for manipulation. If an object is returned, it is used as the
                            // data object to submit
                            var d = conf.data(request);
                            if (d) {
                                $.extend(request, d);
                            }
                        }
                        else if ($.isPlainObject(conf.data)) {
                            // As an object, the data given extends the default
                            $.extend(request, conf.data);
                        }

                        settings.jqXHR = $.ajax({
                            "type": conf.method,
                            "url": conf.url,
                            "data": request,
                            "dataType": "json",
                            "cache": false,
                            "success": function (json) {
                                var data = {
                                    "recordsTotal": json.body.total,
                                    "recordsFiltered": json.body.total,
                                    "data": json.body.list
                                }
                                cacheLastJson = $.extend(true, {}, data);

                                if (cacheLower != drawStart) {
                                    data.data.splice(0, drawStart - cacheLower);
                                }
                                data.data.splice(requestLength, data.data.length);

                                drawCallback(data);
                            }
                        });
                    }
                    else {
                        var json = $.extend(true, {}, cacheLastJson);
                        json.draw = request.draw; // Update the echo for each response
                        json.data.splice(0, requestStart - cacheLower);
                        json.data.splice(requestLength, json.data.length);

                        drawCallback(json);
                    }
                }
            };

            // Register an API method that will empty the pipelined data, forcing an Ajax
            // fetch on the next draw (i.e. `table.clearPipeline().draw()`)
            $.fn.dataTable.Api.register('clearPipeline()', function () {
                return this.iterator('table', function (settings) {
                    settings.clearCache = true;
                });
            });
        }
    };
    App.Utils.UrlUtil = {
        getProjectRoot: function () {
            var localhostPaht = this.getRoot();
            //获取带"/"的项目名，如：/uimcardprj
            var pathName = window.document.location.pathname;
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
            return (localhostPaht + projectName);
        },
        getRoot: function () {
            //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
            var curWwwPath = window.document.location.href;
            //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            //获取主机地址，如： http://localhost:8083
            var localhostPaht = curWwwPath.substring(0, pos);
            return localhostPaht;
        },
        getUrlParameter: function (key) {
            var reg = new RegExp("(^|&)" + key + "=([^&]*)(&|$)");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]);
            return null;
        }
    };
    App.Utils.StringUtil = {
        md5: function (string) {
            return md5(string);
        }

    }
    function md5(string) {
        function md5_RotateLeft(lValue, iShiftBits) {
            return (lValue << iShiftBits) | (lValue >>> (32 - iShiftBits));
        }

        function md5_AddUnsigned(lX, lY) {
            var lX4, lY4, lX8, lY8, lResult;
            lX8 = (lX & 0x80000000);
            lY8 = (lY & 0x80000000);
            lX4 = (lX & 0x40000000);
            lY4 = (lY & 0x40000000);
            lResult = (lX & 0x3FFFFFFF) + (lY & 0x3FFFFFFF);
            if (lX4 & lY4) {
                return (lResult ^ 0x80000000 ^ lX8 ^ lY8);
            }
            if (lX4 | lY4) {
                if (lResult & 0x40000000) {
                    return (lResult ^ 0xC0000000 ^ lX8 ^ lY8);
                } else {
                    return (lResult ^ 0x40000000 ^ lX8 ^ lY8);
                }
            } else {
                return (lResult ^ lX8 ^ lY8);
            }
        }

        function md5_F(x, y, z) {
            return (x & y) | ((~x) & z);
        }

        function md5_G(x, y, z) {
            return (x & z) | (y & (~z));
        }

        function md5_H(x, y, z) {
            return (x ^ y ^ z);
        }

        function md5_I(x, y, z) {
            return (y ^ (x | (~z)));
        }

        function md5_FF(a, b, c, d, x, s, ac) {
            a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_F(b, c, d), x), ac));
            return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };

        function md5_GG(a, b, c, d, x, s, ac) {
            a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_G(b, c, d), x), ac));
            return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };

        function md5_HH(a, b, c, d, x, s, ac) {
            a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_H(b, c, d), x), ac));
            return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };

        function md5_II(a, b, c, d, x, s, ac) {
            a = md5_AddUnsigned(a, md5_AddUnsigned(md5_AddUnsigned(md5_I(b, c, d), x), ac));
            return md5_AddUnsigned(md5_RotateLeft(a, s), b);
        };

        function md5_ConvertToWordArray(string) {
            var lWordCount;
            var lMessageLength = string.length;
            var lNumberOfWords_temp1 = lMessageLength + 8;
            var lNumberOfWords_temp2 = (lNumberOfWords_temp1 - (lNumberOfWords_temp1 % 64)) / 64;
            var lNumberOfWords = (lNumberOfWords_temp2 + 1) * 16;
            var lWordArray = Array(lNumberOfWords - 1);
            var lBytePosition = 0;
            var lByteCount = 0;
            while (lByteCount < lMessageLength) {
                lWordCount = (lByteCount - (lByteCount % 4)) / 4;
                lBytePosition = (lByteCount % 4) * 8;
                lWordArray[lWordCount] = (lWordArray[lWordCount] | (string.charCodeAt(lByteCount) << lBytePosition));
                lByteCount++;
            }
            lWordCount = (lByteCount - (lByteCount % 4)) / 4;
            lBytePosition = (lByteCount % 4) * 8;
            lWordArray[lWordCount] = lWordArray[lWordCount] | (0x80 << lBytePosition);
            lWordArray[lNumberOfWords - 2] = lMessageLength << 3;
            lWordArray[lNumberOfWords - 1] = lMessageLength >>> 29;
            return lWordArray;
        };

        function md5_WordToHex(lValue) {
            var WordToHexValue = "",
                WordToHexValue_temp = "",
                lByte, lCount;
            for (lCount = 0; lCount <= 3; lCount++) {
                lByte = (lValue >>> (lCount * 8)) & 255;
                WordToHexValue_temp = "0" + lByte.toString(16);
                WordToHexValue = WordToHexValue + WordToHexValue_temp.substr(WordToHexValue_temp.length - 2, 2);
            }
            return WordToHexValue;
        };

        function md5_Utf8Encode(string) {
            string = string.replace(/\r\n/g, "\n");
            var utftext = "";
            for (var n = 0; n < string.length; n++) {
                var c = string.charCodeAt(n);
                if (c < 128) {
                    utftext += String.fromCharCode(c);
                } else if ((c > 127) && (c < 2048)) {
                    utftext += String.fromCharCode((c >> 6) | 192);
                    utftext += String.fromCharCode((c & 63) | 128);
                } else {
                    utftext += String.fromCharCode((c >> 12) | 224);
                    utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                    utftext += String.fromCharCode((c & 63) | 128);
                }
            }
            return utftext;
        };
        var x = Array();
        var k, AA, BB, CC, DD, a, b, c, d;
        var S11 = 7,
            S12 = 12,
            S13 = 17,
            S14 = 22;
        var S21 = 5,
            S22 = 9,
            S23 = 14,
            S24 = 20;
        var S31 = 4,
            S32 = 11,
            S33 = 16,
            S34 = 23;
        var S41 = 6,
            S42 = 10,
            S43 = 15,
            S44 = 21;
        string = md5_Utf8Encode(string);
        x = md5_ConvertToWordArray(string);
        a = 0x67452301;
        b = 0xEFCDAB89;
        c = 0x98BADCFE;
        d = 0x10325476;
        for (k = 0; k < x.length; k += 16) {
            AA = a;
            BB = b;
            CC = c;
            DD = d;
            a = md5_FF(a, b, c, d, x[k + 0], S11, 0xD76AA478);
            d = md5_FF(d, a, b, c, x[k + 1], S12, 0xE8C7B756);
            c = md5_FF(c, d, a, b, x[k + 2], S13, 0x242070DB);
            b = md5_FF(b, c, d, a, x[k + 3], S14, 0xC1BDCEEE);
            a = md5_FF(a, b, c, d, x[k + 4], S11, 0xF57C0FAF);
            d = md5_FF(d, a, b, c, x[k + 5], S12, 0x4787C62A);
            c = md5_FF(c, d, a, b, x[k + 6], S13, 0xA8304613);
            b = md5_FF(b, c, d, a, x[k + 7], S14, 0xFD469501);
            a = md5_FF(a, b, c, d, x[k + 8], S11, 0x698098D8);
            d = md5_FF(d, a, b, c, x[k + 9], S12, 0x8B44F7AF);
            c = md5_FF(c, d, a, b, x[k + 10], S13, 0xFFFF5BB1);
            b = md5_FF(b, c, d, a, x[k + 11], S14, 0x895CD7BE);
            a = md5_FF(a, b, c, d, x[k + 12], S11, 0x6B901122);
            d = md5_FF(d, a, b, c, x[k + 13], S12, 0xFD987193);
            c = md5_FF(c, d, a, b, x[k + 14], S13, 0xA679438E);
            b = md5_FF(b, c, d, a, x[k + 15], S14, 0x49B40821);
            a = md5_GG(a, b, c, d, x[k + 1], S21, 0xF61E2562);
            d = md5_GG(d, a, b, c, x[k + 6], S22, 0xC040B340);
            c = md5_GG(c, d, a, b, x[k + 11], S23, 0x265E5A51);
            b = md5_GG(b, c, d, a, x[k + 0], S24, 0xE9B6C7AA);
            a = md5_GG(a, b, c, d, x[k + 5], S21, 0xD62F105D);
            d = md5_GG(d, a, b, c, x[k + 10], S22, 0x2441453);
            c = md5_GG(c, d, a, b, x[k + 15], S23, 0xD8A1E681);
            b = md5_GG(b, c, d, a, x[k + 4], S24, 0xE7D3FBC8);
            a = md5_GG(a, b, c, d, x[k + 9], S21, 0x21E1CDE6);
            d = md5_GG(d, a, b, c, x[k + 14], S22, 0xC33707D6);
            c = md5_GG(c, d, a, b, x[k + 3], S23, 0xF4D50D87);
            b = md5_GG(b, c, d, a, x[k + 8], S24, 0x455A14ED);
            a = md5_GG(a, b, c, d, x[k + 13], S21, 0xA9E3E905);
            d = md5_GG(d, a, b, c, x[k + 2], S22, 0xFCEFA3F8);
            c = md5_GG(c, d, a, b, x[k + 7], S23, 0x676F02D9);
            b = md5_GG(b, c, d, a, x[k + 12], S24, 0x8D2A4C8A);
            a = md5_HH(a, b, c, d, x[k + 5], S31, 0xFFFA3942);
            d = md5_HH(d, a, b, c, x[k + 8], S32, 0x8771F681);
            c = md5_HH(c, d, a, b, x[k + 11], S33, 0x6D9D6122);
            b = md5_HH(b, c, d, a, x[k + 14], S34, 0xFDE5380C);
            a = md5_HH(a, b, c, d, x[k + 1], S31, 0xA4BEEA44);
            d = md5_HH(d, a, b, c, x[k + 4], S32, 0x4BDECFA9);
            c = md5_HH(c, d, a, b, x[k + 7], S33, 0xF6BB4B60);
            b = md5_HH(b, c, d, a, x[k + 10], S34, 0xBEBFBC70);
            a = md5_HH(a, b, c, d, x[k + 13], S31, 0x289B7EC6);
            d = md5_HH(d, a, b, c, x[k + 0], S32, 0xEAA127FA);
            c = md5_HH(c, d, a, b, x[k + 3], S33, 0xD4EF3085);
            b = md5_HH(b, c, d, a, x[k + 6], S34, 0x4881D05);
            a = md5_HH(a, b, c, d, x[k + 9], S31, 0xD9D4D039);
            d = md5_HH(d, a, b, c, x[k + 12], S32, 0xE6DB99E5);
            c = md5_HH(c, d, a, b, x[k + 15], S33, 0x1FA27CF8);
            b = md5_HH(b, c, d, a, x[k + 2], S34, 0xC4AC5665);
            a = md5_II(a, b, c, d, x[k + 0], S41, 0xF4292244);
            d = md5_II(d, a, b, c, x[k + 7], S42, 0x432AFF97);
            c = md5_II(c, d, a, b, x[k + 14], S43, 0xAB9423A7);
            b = md5_II(b, c, d, a, x[k + 5], S44, 0xFC93A039);
            a = md5_II(a, b, c, d, x[k + 12], S41, 0x655B59C3);
            d = md5_II(d, a, b, c, x[k + 3], S42, 0x8F0CCC92);
            c = md5_II(c, d, a, b, x[k + 10], S43, 0xFFEFF47D);
            b = md5_II(b, c, d, a, x[k + 1], S44, 0x85845DD1);
            a = md5_II(a, b, c, d, x[k + 8], S41, 0x6FA87E4F);
            d = md5_II(d, a, b, c, x[k + 15], S42, 0xFE2CE6E0);
            c = md5_II(c, d, a, b, x[k + 6], S43, 0xA3014314);
            b = md5_II(b, c, d, a, x[k + 13], S44, 0x4E0811A1);
            a = md5_II(a, b, c, d, x[k + 4], S41, 0xF7537E82);
            d = md5_II(d, a, b, c, x[k + 11], S42, 0xBD3AF235);
            c = md5_II(c, d, a, b, x[k + 2], S43, 0x2AD7D2BB);
            b = md5_II(b, c, d, a, x[k + 9], S44, 0xEB86D391);
            a = md5_AddUnsigned(a, AA);
            b = md5_AddUnsigned(b, BB);
            c = md5_AddUnsigned(c, CC);
            d = md5_AddUnsigned(d, DD);
        }
        return (md5_WordToHex(a) + md5_WordToHex(b) + md5_WordToHex(c) + md5_WordToHex(d)).toLowerCase();
    }

    jQuery.extend({
        getUrlVars: function () {
            var vars = [], hash;
            var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
            for (var i = 0; i < hashes.length; i++) {
                hash = hashes[i].split('=');
                vars.push(hash[0]);
                vars[hash[0]] = hash[1];
            }
            return vars;
        },
        getUrlVar: function (name) {
            return jQuery.getUrlVars()[name];
        }
    });

})(jQuery, $.AdminLTE, App);