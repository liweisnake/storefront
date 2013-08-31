

var loadingImage = 'loading.gif';
var closeButton = 'close.gif';
var isModalLoaded = false
//
// getPageScroll()
// Returns array with x,y page scroll values.
// Core code from - quirksmode.org
//
function getPageScroll(){

    var yScroll;

    if (self.pageYOffset) {
        yScroll = self.pageYOffset;
    } else if (document.documentElement && document.documentElement.scrollTop){     // Explorer 6 Strict
        yScroll = document.documentElement.scrollTop;
    } else if (document.body) {// all other Explorers
        yScroll = document.body.scrollTop;
    }

    arrayPageScroll = new Array('',yScroll)
    return arrayPageScroll;
}


//
// getPageSize()
// Returns array with page width, height and window width, height
// Core code from - quirksmode.org
//
function getPageSize(){

    var xScroll, yScroll;

    if (window.innerHeight && window.scrollMaxY) {
        xScroll = document.body.scrollWidth;
        yScroll = window.innerHeight + window.scrollMaxY;
    } else if (document.body.scrollHeight > document.body.offsetHeight){ // all but Explorer Mac
        xScroll = document.body.scrollWidth;
        yScroll = document.body.scrollHeight;
    } else { // Explorer Mac...would also work in Explorer 6 Strict, Mozilla and Safari
        xScroll = document.body.offsetWidth;
        yScroll = document.body.offsetHeight;
    }

    var windowWidth, windowHeight;
    if (self.innerHeight) {    // all except Explorer
        windowWidth = self.innerWidth;
        windowHeight = self.innerHeight;
    } else if (document.documentElement && document.documentElement.clientHeight) { // Explorer 6 Strict Mode
        windowWidth = document.documentElement.clientWidth;
        windowHeight = document.documentElement.clientHeight;
    } else if (document.body) { // other Explorers
        windowWidth = document.body.clientWidth;
        windowHeight = document.body.clientHeight;
    }

    // for small pages with total height less then height of the viewport
    if(yScroll < windowHeight){
        pageHeight = windowHeight;
    } else {
        pageHeight = yScroll;
    }

    // for small pages with total width less then width of the viewport
    if(xScroll < windowWidth){
        pageWidth = windowWidth;
    } else {
        pageWidth = xScroll;
    }


    arrayPageSize = new Array(pageWidth,pageHeight,windowWidth,windowHeight)
    return arrayPageSize;
}


//
// pause(numberMillis)
// Pauses code execution for specified time. Uses busy code, not good.
// Code from http://www.faqts.com/knowledge_base/view.phtml/aid/1602
//
function pause(numberMillis) {
    var now = new Date();
    var exitTime = now.getTime() + numberMillis;
    while (true) {
        now = new Date();
        if (now.getTime() > exitTime)
            return;
    }
}

//
// getKey(key)
// Gets keycode. If 'x' is pressed then it hides the Modal.
//

function getKey(e){
    if (e == null) { // ie
        keycode = event.keyCode;
    } else { // mozilla
        keycode = e.which;
    }
    key = String.fromCharCode(keycode).toLowerCase();

    if(key == 'x'){ hideContentModal(); }
}


//
// listenKey()
//
function listenKey () {
    document.onkeypress = getKey;
}


//
// showModal()
// Preloads images. Pleaces new image in Modal then centers and displays.
//
function showModal(objLink) {
    try{
        // prep objects
        var objOverlay = document.getElementById('overlay');
        var objModal = document.getElementById('modal');
        var objCaption = document.getElementById('modal-caption');
        var objImage = document.getElementById('modal-image');
        var objLoadingImage = document.getElementById('loadingImage');
        var objModalDetails = document.getElementById('modal-details');

        var arrayPageSize = getPageSize();
        var arrayPageScroll = getPageScroll();

        // center loadingImage if it exists
        if (objLoadingImage) {
            objLoadingImage.style.top = (arrayPageScroll[1] + ((arrayPageSize[3] - 35 - objLoadingImage.height) / 2) + 'px');
            objLoadingImage.style.left = (((arrayPageSize[0] - 20 - objLoadingImage.width) / 2) + 'px');
            objLoadingImage.style.display = 'block';
        }

        // set height and width of Overlay to take up whole page and show
        objOverlay.style.height = (arrayPageSize[1] + 'px');
        objOverlay.style.width = (arrayPageSize[2] + 'px');
        objOverlay.style.display = 'block';

        // preload image
        imgPreload = new Image();

        imgPreload.onload=function(){
            objImage.src = objLink;

            // center Modal and make sure that the top and left values are not negative
            // and the image placed outside the viewport
            var ModalTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - imgPreload.height) / 2);
            var ModalLeft = ((arrayPageSize[0] - 20 - imgPreload.width) / 2);

            objModal.style.top = (ModalTop < 0) ? "0px" : ModalTop + "px";
            objModal.style.left = (ModalLeft < 0) ? "0px" : ModalLeft + "px";


            objModalDetails.style.width = imgPreload.width + 'px';

            //objCaption.innerHTML = "Loading...";


            // A small pause between the image loading and displaying is required with IE,
            // this prevents the previous image displaying for a short burst causing flicker.
            if (navigator.appVersion.indexOf("MSIE")!=-1){
                pause(250);
            }

            if (objLoadingImage) {
                objLoadingImage.style.display = 'none';
            }

            // Hide select boxes as they will 'peek' through the image in IE
            //TODO - Need to add a condition form ajax submit so these are hidden during modal showing
            //TODO - For now I will comment out
            selects = document.getElementsByTagName("select");
            //for (i = 0; i != selects.length; i++) {
                    //selects[i].style.visibility = "hidden";
            //}

            objModal.style.display = 'block';

            // After image is loaded, update the overlay height as the new image might have
            // increased the overall page height.
            arrayPageSize = getPageSize();
            objOverlay.style.height = (arrayPageSize[1] + 'px');

            // Check for 'x' keypress
            listenKey();

            return false;
        }

        imgPreload.src = objLink;

    }catch(Exception){

    }
}

//
// hideModal()
//
function hideModal()
{
    // get objects
    objOverlay = document.getElementById('overlay');
    objModal = document.getElementById('modal');

    // hide Modal and overlay
    objOverlay.style.display = 'none';
    objModal.style.display = 'none';

    // make select boxes visible
    selects = document.getElementsByTagName("select");
    for (i = 0; i != selects.length; i++) {
        selects[i].style.visibility = "visible";
    }

    // disable keypress listener
    document.onkeypress = '';

}

function hideContentModal(modalId)
{
    // get objects
    objModal = document.getElementById(modalId + '-modal');

    document.getElementById(modalId + "-overlay").style.display = 'none';

    // hide Modal and overlay
    objModal.style.display = 'none';

    // make select boxes visible
    selects = document.getElementsByTagName("select");
    for (i = 0; i != selects.length; i++) {
        selects[i].style.visibility = "visible";
    }

    // disable keypress listener
    //document.onkeypress = '';

}

//
// initLoadingModal()
//
function initModal()
{
    try{
        var objBody = document.getElementById("loading-modal");

        // create overlay div and hardcode some functional styles (aesthetic styles are in CSS file)
        var objOverlay = document.createElement("div");
        objOverlay.setAttribute('id','overlay');
        objOverlay.className = 'overlay';
        //objOverlay.onclick = function () {hideModal(); return false;}
        objOverlay.style.display = 'none';
        objOverlay.style.position = 'absolute';
        objOverlay.style.top = '0';
        objOverlay.style.left = '0';
        objOverlay.style.zIndex = '101';
        objOverlay.style.width = '100%';
        objBody.insertBefore(objOverlay, objBody.firstChild);
        var arrayPageSize = getPageSize();
        var arrayPageScroll = getPageScroll();

        // preload and create loader image
        var imgPreloader = new Image();

        // if loader image found, create link to hide Modal and create loadingimage
        imgPreloader.onload=function(){

            var objLoadingImageLink = document.createElement("a");
            objLoadingImageLink.setAttribute('href','#');
            objLoadingImageLink.onclick = function () {hideModal(); return false;}
            objOverlay.appendChild(objLoadingImageLink);

            var objLoadingImage = document.createElement("img");
            objLoadingImage.src = loadingImage;
            objLoadingImage.setAttribute('id','loading-image');
            objLoadingImage.style.position = 'absolute';
            objLoadingImage.style.zIndex = '150';
            objLoadingImageLink.appendChild(objLoadingImage);

            imgPreloader.onload=function(){};    //    clear onLoad, as IE will flip out w/animated gifs

            return false;
        }

        imgPreloader.src = loadingImage;

        // create Modal div, same note about styles as above
        var objModal = document.createElement("div");
        objModal.setAttribute('id','modal');
        objModal.className = 'modal';
        objModal.style.display = 'none';
        objModal.style.position = 'absolute';
        objModal.style.zIndex = '102';
        objBody.insertBefore(objModal, objOverlay.nextSibling);

        // create link
        var objLink = document.createElement("a");
        objLink.setAttribute('href','#');
        objLink.setAttribute('title','Click to close');
        objLink.onclick = function () {hideModal(); return false;}
        objModal.appendChild(objLink);

        // preload and create close button image
        var imgPreloadCloseButton = new Image();

        // if close button image found,
        imgPreloadCloseButton.onload=function(){

            var objCloseButton = document.createElement("img");
            objCloseButton.src = closeButton;
            objCloseButton.setAttribute('id','close-button');
            objCloseButton.style.position = 'absolute';
            objCloseButton.style.zIndex = '200';
            objLink.appendChild(objCloseButton);

            return false;
        }

        imgPreloadCloseButton.src = closeButton;

        // create image
        var objImage = document.createElement("img");
        objImage.setAttribute('id','modal-image');
        objLink.appendChild(objImage);

        // create details div, a container for the caption and keyboard message
        var objModalDetails = document.createElement("div");
        objModalDetails.setAttribute('id','modal-details');
        objModalDetails.className = 'modal-details';
        objModal.appendChild(objModalDetails);

        // create caption
        var objCaption = document.createElement("div");
        objCaption.setAttribute('id','modal-caption');
        objCaption.className = 'modal-caption';
        objCaption.style.display = 'none';
        objModalDetails.appendChild(objCaption);

        // create keyboard message
/*
        var objLoadingMsg = document.createElement("div");
        objLoadingMsg.setAttribute('id','loadingMsg');
        objLoadingMsg.className = 'loadingMsg';
        objLoadingMsg.innerHTML = 'Loading...';
        objModalDetails.appendChild(objLoadingMsg);
*/
        showModal('/portal/images/modal/loading.gif');

    }catch(Exception){

    }
}

//#######################ContentModal

function alertModal(modalId,msgContainer){
    if (modalId == null){
        modalId = 'content-modal';
    }
    if (msgContainer == null) {
        msgContainer = 'message-container';
    }
    initContentModal(modalId);
    showContentModal(modalId,msgContainer);
}
//
// showModal()
// Preloads images. Pleaces new image in Modal then centers and displays.
//
function showContentModal(modalId,msgContainer) {
    try{
        var messageContainer = (msgContainer ? msgContainer : 'message-container');

        // prep objects
        var objModalContainer = document.getElementById(modalId);
        var objOverlay = document.getElementById(modalId + '-overlay');
        var objModal = document.getElementById(modalId + '-modal');
        var objCaption = document.getElementById(modalId + '-modal-caption');
        var objImage = document.getElementById(modalId + '-modal-image');
        var objLoadingImage = document.getElementById(modalId + '-loadingImage');
        //var objModalDetails = document.getElementById('modal-details');
        var contentBody = document.getElementById(messageContainer);
        var contentWidth = contentBody.style.width.substring(0,contentBody.style.width.indexOf("px"));
        var contentHeight = contentBody.style.height.substring(0,contentBody.style.width.indexOf("px"));

        var arrayPageSize = getPageSize();
        var arrayPageScroll = getPageScroll();

        // center loadingImage if it exists
        if (objLoadingImage) {
            objLoadingImage.style.top = (arrayPageScroll[1] + ((arrayPageSize[3] - 35 - objLoadingImage.height) / 2) + 'px');
            objLoadingImage.style.left = (((arrayPageSize[0] - 20 - objLoadingImage.width) / 2) + 'px');
            objLoadingImage.style.display = 'block';
        }
        // set height and width of Overlay to take up whole page and show
        objOverlay.style.height = (arrayPageSize[1] + 'px');
        objOverlay.style.width = (arrayPageSize[2] + 'px');
        objOverlay.style.display = 'block';



            // center Modal and make sure that the top and left values are not negative
            // and the image placed outside the viewport
            var ModalTop = arrayPageScroll[1] + ((arrayPageSize[3] - 35 - contentHeight) / 2);
            var ModalLeft = ((arrayPageSize[0] - 20 - contentWidth) / 2);

            objModal.style.top = (ModalTop < 0) ? "0px" : ModalTop + "px";
            objModal.style.left = (ModalLeft < 0) ? "0px" : ModalLeft + "px";


            objModal.style.width = contentWidth + 'px';

            //objCaption.innerHTML = document.getElementById("errorMessages").innerHTML;


            // A small pause between the image loading and displaying is required with IE,
            // this prevents the previous image displaying for a short burst causing flicker.
            if (navigator.appVersion.indexOf("MSIE")!=-1){
                pause(250);
            }

            if (objLoadingImage) {
                objLoadingImage.style.display = 'none';
            }

            // Hide select boxes as they will 'peek' through the image in IE

            selects = document.getElementsByTagName("select");
            //had to hard-code a check to see if we are forcing this to be hidden.
            //if so, then do not hide selectboxes; because the modal is not being shown
            //alert(document.getElementById("content-modal").style.display == 'none');
            var hideSelects = true;
            try{
                if (!document.getElementById(modalId).style.display == 'none'){
                    hideSelects = false;
                }
            }catch(Exception){
                //alert(Exception);
            }

            if (hideSelects || !document.getElementById(modalId).style.display == 'none'){
                //alert("test");
                for (i = 0; i != selects.length; i++) {
                    selects[i].style.visibility = "hidden";
                }
            }

            objModal.style.display = 'block';
            objModalContainer.style.display = 'block';
            // After image is loaded, update the overlay height as the new image might have
            // increased the overall page height.
            arrayPageSize = getPageSize();
            objOverlay.style.height = (arrayPageSize[1] + 'px');

            // Check for 'x' keypress
            listenKey();

    }catch(Exception){

    }
}

//
// initLoadingModal()
//
function initContentModal(modalId)
{

    try{
        if (modalId == null){
            modalId = 'content-modal';
        }

        var objBody = document.getElementById(modalId);
        //get the first div - which is message-container
        var contentBody = objBody.getElementsByTagName('div')[0];

        var contentWidth = contentBody.style.width.substring(0,contentBody.style.width.indexOf("px"));
        var contentHeight = contentBody.style.height.substring(0,contentBody.style.width.indexOf("px"));

        var hasObjOverlay = document.getElementById(modalId + '-overlay');

        if(!hasObjOverlay) { //don't create if already exist.
            // create overlay div and hardcode some functional styles (aesthetic styles are in CSS file)
            var objOverlay = document.createElement("div");
            objOverlay.setAttribute('id',modalId + '-overlay');
            objOverlay.className = 'overlay';

            objOverlay.style.display = 'none';
            objOverlay.style.position = 'absolute';
            objOverlay.style.top = '0';
            objOverlay.style.left = '0';
            objOverlay.style.zIndex = '101';
            objOverlay.style.width = '100%';
            objOverlay.onclick = function (){hideContentModal(modalId); return false};

            objBody.insertBefore(objOverlay, objBody.firstChild);

            var arrayPageSize = getPageSize();
            var arrayPageScroll = getPageScroll();

            // create Modal div, same note about styles as above
            var objModal = document.createElement("div");
            objModal.setAttribute('id',modalId + '-modal');
            objModal.className = 'modal';
            objModal.style.display = 'none';
            objModal.style.position = 'absolute';
            objModal.style.zIndex = '102';
            objModal.style.width = contentWidth + 'px';
            objModal.style.height = contentHeight + 'px';
            objModal.innerHTML = contentBody.innerHTML;
            objBody.insertBefore(objModal, objOverlay.nextSibling);
        }
    }catch(Exception){

    }
}

function getComponentSize(componentId){

    var componentWidth;
    var componentHeight;
    var componentTop;
    var componentLeft;

    var component = document.getElementById(componentId);

    componentWidth = component.clientWidth;
    componentHeight = component.clientHeight;
    componentLeft = component.offsetLeft;
    componentTop = component.offsetTop;

    return new Array(componentWidth,componentHeight,componentLeft,componentTop);

}

//This creates a modal window per component.
//currently used during ajax calls only in the a4j:status tag
function createComponentModal(componentId){
    try{
        var loadingModal = document.getElementById("loading-modal");

        var loadingImage = "/portal/images/modal/loading.gif"

        var arrayComponentSize = getComponentSize(componentId);
        var overlayTop = (arrayComponentSize[3]);
        var overlayLeft = (arrayComponentSize[2]);
        var overlayHeight = (arrayComponentSize[1]);
        var overlayWidth = (arrayComponentSize[0]);

        // create overlay div and hardcode some functional styles (aesthetic styles are in CSS file)
        var objOverlay = document.getElementById(componentId + 'overlay');

        if (!objOverlay){
            objOverlay = document.createElement("div");
            objOverlay.setAttribute('id',componentId + 'overlay');
            objOverlay.className = 'overlay';
            objOverlay.style.position = 'absolute';
            objOverlay.style.display = 'block';
        }

        objOverlay.style.height = (overlayHeight + 'px');
        objOverlay.style.width = (overlayWidth + 'px');


        loadingModal.insertBefore(objOverlay, loadingModal.firstChild);

        // create Modal div, same note about styles as above
        var objModal = document.getElementById(componentId + 'modal');

        if (!objModal){
            objModal = document.createElement("div");
            objModal.setAttribute('id',componentId + 'modal');
            objModal.className = 'modal';
            objOverlay.appendChild(objModal);
        }

        // preload and create loader image
        var imgPreloader = new Image();

        // if loader image found, create link to hide Modal and create loadingimage
        //imgPreloader.onload=function(){

            /*var objLoadingImageLink = document.getElementById(componentId + 'ImageLink');

            if (!objLoadingImageLink){
                objLoadingImageLink.setAttribute('href','#');
                objLoadingImageLink.onclick = function () {hideModal(); return false;}
                objLoadingImageLink.setAttribute('id',componentId + 'ImageLink');
                objModal.appendChild(objLoadingImageLink);
            }*/

            var objLoadingImageWidth = "100";
            var objLoadingImage = document.getElementById(componentId + 'loading-image')

            if(!objLoadingImage){
                objLoadingImage = document.createElement("img");
                objLoadingImage.src = loadingImage;
                objLoadingImage.setAttribute('id',componentId + 'loading-image');
                objLoadingImage.style.position = 'relative';
                objLoadingImage.style.width = objLoadingImageWidth + 'px';
                objModal.appendChild(objLoadingImage);
            }

            imgPreloader.onload=function(){};    //    clear onLoad, as IE will flip out w/animated gifs

            if (objLoadingImage) {
                objLoadingImage.style.display = 'block';
            }

            var ModalTop = ((overlayHeight - 35 - imgPreloader.height) / 2);
            var ModalLeft = ((overlayWidth - 20 - objLoadingImageWidth) / 2);

            objModal.style.top = (ModalTop < 0) ? "0px" : ModalTop + "px";
            objModal.style.left = (ModalLeft < 0) ? "0px" : ModalLeft + "px";
            objModal.style.width = objLoadingImageWidth + 'px';
            objModal.style.display = 'block';
            objModal.style.position = 'relative';



            if (navigator.appVersion.indexOf("MSIE")!=-1){
                pause(250);
            }

            objOverlay.style.top = (arrayComponentSize[3] + 'px');
            objOverlay.style.left = (arrayComponentSize[2] + 'px');
            objOverlay.style.height = (arrayComponentSize[1] + 'px');
            objOverlay.style.width = (arrayComponentSize[0] + 'px');
            objOverlay.style.zIndex = 0;
            objOverlay.style.position = 'absolute';

            return false;
        //}

        imgPreloader.src = loadingImage;


    }catch(Exception){

    }

}

