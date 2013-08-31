JBoss Portal theme information
November 21, 2005

Ingredients:
1) Theme name - "Maple"
2) Theme notes - Description or concept of theme.
3) Custom branding - Removal of the JBoss branding graphic in the header.
4) Theme author/designer - Mark Fernandes, Novell
5) Additional credits

THEME NOTES:
This theme was designed to present the versatility of the CSS-driven DIV-Renderer method of controlling the portal UI.

This theme includes styles for the header navigation and portal login page. It also includes (via the css), the following copyright text: "Theme by Novell". Note that this text will not appear in IE as the css property that was used ('content' property) is not supported by the browser at this time.

CUSTOM BRANDING:
To remove, replace or hide the header graphic that contains the JBoss product branding text (the "JBoss Portal" text in the top-right-hand corner of the screen), open the "portal_style.css" file and modify the #logoName selector.

	#logoName {
	/* Logo...*/
	   background-image: url(images/logo.gif);
	   background-repeat: no-repeat;
	   width: 198px;
           height: 62px;
	   z-index: 2;
	   position: absolute;
	   right: 16px;
	   top: 13px;
	}

You can easily just replace the background image with a custom graphic, or simply comment it out. Adjust the height and width attributes accordingly when replacing this image if necessary. This same approach can also be taken with the entire header background graphic (css selector: #header-container).

CREDITS:
* Maple tree photo, artwork, and CSS coding by Mark Fernandes. Released under Creative Commons License (by-sa).
* Copyright GNU LGPL (c) 2005 Novell, Inc.