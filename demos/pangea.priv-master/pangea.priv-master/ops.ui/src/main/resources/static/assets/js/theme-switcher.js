var themes = {
	"barclays" : {desc:"Barclays",url:"/assets/css/barclays.css"},
	"blue" : {desc:"BATS",url:"/assets/css/custom.css"},
	"red-black" : {desc:"Super-BATS",url:"/assets/css/custom-red.css"},
	"red-yellow" : {desc:"Purdy-BATS",url:"/assets/css/custom-red-yellow.css"},
	"green-gold" : {desc:"ANTS",url:"/assets/css/custom-green-gold.css"},
	"bl-blue" : {desc:"CATS",url:"/assets/css/bl-blue.css"}
}

var activeTheme = "barclays";

var switchTitle = function(){
	//document.title = "";
	$('.applicationtitle').each(function(){
		var title = window.getComputedStyle(this,':before').content;
		//JHP - Temporarily decommissioned
		//document.title = title.replace(new RegExp('"', 'g'),'');
	});
}

var initThemeSwitcher = function (theme) {
	var themesheet = $('<link href="' + themes[theme].url + '" rel="stylesheet" />');
	themesheet.appendTo('head');
	switchTitle();
	
	$('.theme-link').click(function() {
		var themeurl = themes[$(this).attr('data-theme')].url;
		themesheet.attr('href', themeurl);
		switchTitle();
	});
};