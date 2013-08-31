
var Q = jQuery.noConflict();
Q(document).ready(function () {
    if(Q(".left"))
		Q("img:.left").not('.bigThumbnail').hover(function(){zoom(this)},function(){Q('#zoom_photo').hide("slow");});	
	//Q('#platH').prepend('<p class=handlemax onclick="toggl(this,\'#platForm\');"/>');
	//Q('#cateH').prepend('<p class=handlemax onclick="toggl(this,\'#categoryPanel\');"/>');
});


function zoom(img) {
	var pos=Q(img).offset();
	if(Q('#zoom_photo'))
		Q('div').remove('#zoom_photo');
	var div='<div id="zoom_photo" style="display:none;z-index:9;position: absolute;left:'+(pos.left+img.width+10)+'px;top:'+pos.top+'px;"></div>';
	Q(img).after(div); 
	Q(img).css('cursor','pointer');
	var img1=Q(img).clone();
	img1.attr({width:img.width*4,height:img.height*4});
	Q('#zoom_photo').html(img1);
	Q('#zoom_photo').show("slow");
}

function toggl(o,region) {
	Q(o).toggleClass('handlemin');
	Q('.panelContent',region).slideToggle("slow");
}

//