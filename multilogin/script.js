// ==UserScript==
// @name         New Userscript
// @namespace    http://tampermonkey.net/
// @version      0.1
// @description  try to take over the world!
// @author       You
// @match        *://*/*
// @grant        GM_setValue
// @grant        GM_getValue
// @require http://userscripts-mirror.org/scripts/source/107941.user.js
// ==/UserScript==

(function() {
    var loca = (document.location + "");
    var json = "", currentJSON = null;
    String.prototype.indexOfRegex = function(regex){
        var match = this.match(regex);
        return match ? this.indexOf(match[0]) : -1;
    }
    function triggerMouseEvent (node, eventType) {
        var clickEvent = document.createEvent ('MouseEvents');
        clickEvent.initEvent (eventType, true, true);
        node.dispatchEvent (clickEvent);
    }
    console.log(loca);
    if(loca.indexOf("http://example.com/?data=") != -1){
        json = JSON.parse(decodeURIComponent(loca).split("data=")[1] + "");
		for(var a = 0;a<json.actions.length;a++){
			var el = json.actions[a];
			var currObj = el.telegram != undefined ?
				el.telegram : el.twitter != undefined ? el.twitter : el.facebook != undefined ? el.facebook : null;
			for(var i = 0;i<currObj.length;i++){
				currentJSON = currObj[i];
				if(currentJSON != null){
					delete currObj[i];
					GM_setValue('json', JSON.stringify(json));
					GM_setValue('currentJSON', JSON.stringify(currentJSON));
					GM_setValue('link', currentJSON.link);
					window.location = currentJSON.link;
					break;
				}
			}
		}

       console.log("REDIRECT TO");
    }
    if(loca.indexOf("?p=@") != -1 && loca.split("?p=@")[1].indexOfRegex(/[0-9A-Za-z]/) != -1){
        console.log("URA");
        var es = setInterval(function(){
               var chnBut = document.querySelector("body > div.page_wrap > div.im_page_wrap.clearfix > div > div.im_history_col_wrap.noselect.im_history_loaded > div.im_history_selected_wrap > div > div.im_bottom_panel_wrap > div.im_edit_panel_wrap.clearfix.im_edit_small_panel_wrap > div:nth-child(2) > div > a");
               var botBtn = document.querySelector("body > div.page_wrap > div.im_page_wrap.clearfix > div > div.im_history_col_wrap.noselect.im_history_loaded > div.im_history_selected_wrap > div > div.im_bottom_panel_wrap > div.im_edit_panel_wrap.clearfix > div:nth-child(2) > div > a");
               if(typeof chnBut != undefined && chnBut != null){
                   console.log("But val is - " + chnBut.value);
                   chnBut.click();
                   clearInterval(es);
                   json = JSON.parse(GM_getValue('json'));
                   window.location = "http://example.com/?data=" + JSON.stringify(json);
               }
               if((typeof botBtn != undefined) && ( botBtn != null)){
                   botBtn.click();
                   clearInterval(es);
               }
               currentJSON = JSON.parse(GM_getValue('currentJSON'));
               if(currentJSON.type == "botSend"){
                   console.log("BOT URA!");
                   var textInput = document.querySelector("body > div.page_wrap > div.im_page_wrap.clearfix > div > div.im_history_col_wrap.noselect.im_history_loaded > div.im_history_selected_wrap > div > div.im_bottom_panel_wrap > div.im_send_panel_wrap.noselect > div > div > div > form > div.im_send_field_wrap.hasselect.im_send_field_wrap_2ndbtn > div.composer_rich_textarea");
                   var curCom = currentJSON.com;
                   if(curCom.type == "text"){
                       document.querySelector("div.composer_rich_textarea").innerHTML=curCom.value;
                       var targetNode = document.querySelector("body > div.page_wrap > div.im_page_wrap.clearfix > div > div.im_history_col_wrap.noselect.im_history_loaded > div.im_history_selected_wrap > div > div.im_bottom_panel_wrap > div.im_send_panel_wrap.noselect > div > div > div > form > div.im_send_buttons_wrap.clearfix > button > span.im_submit_send_label.nocopy");
                       triggerMouseEvent (targetNode, "mousedown");
                   }
                   clearInterval(es);
               }
        },2000);
    }else if(loca == GM_getValue('link') ){
		console.log("lalalalend");
		json = JSON.parse(GM_getValue('json'));
        window.location = "http://example.com/?data=" + JSON.stringify(json);
	}
})();