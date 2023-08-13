// Auto complete session start
let titleSpinner = $(".title-spinner").hide();
let departmentSpinner = $(".department-spinner").hide();
let addressSpinner = $(".address-spinner").hide();

$("#title").autocomplete({
    minLength: 2,
    source: function(request, response) {
        titleSpinner.show();
        console.log("Spinner loading..")
        $.ajax({
            url: "/fetch-titles", // Replace with your server-side endpoint to fetch names
            data: {
                term: request.term
            },
            success: function(data) {
                if(data.length == 0) {
                    console.log(" + Add new ");
                    response(["+ Add new"]);
                }else {
                    console.log("Spinner hide")
                    response(data);
                }
                titleSpinner.hide()
            }
        });
    },
    open: function(event, ui) {
        var menu = $(this).autocomplete("widget");
        var maxHeight = 200; // Set the maximum height for the suggestion box
        var itemCount = menu.children('li').length;
        menu.css('max-height', maxHeight + 'px');
        if (itemCount > 5) {
            menu.css({
                'overflow-y': 'auto',
                'overflow-x': 'hidden' // Hide the horizontal scrollbar
            });
        } else {
            menu.css('overflow', 'hidden');
        }
        menu.css( {
            "font-size" : "0.8rem",
            "border-radius": "0.25rem"
        });
        menu.find(".ui-menu-item").css( {
            "color" :"#6c757d"});
    },
    select: function(e, ui) {
        var titleValue = (ui.item.value === "+ Add new") ? $('#title').val() : ui.item.value;
        $('#title').val(titleValue);
        return false;
    }
});

$("#department").autocomplete({
    minLength: 2,
    source: function(request, response) {
        departmentSpinner.show();
        console.log("Spinner loading..")
        $.ajax({
            url: "/fetch-departments", // Replace with your server-side endpoint to fetch names
            data: {
                term: request.term
            },
            success: function(data) {
                if(data.length == 0) {
                    console.log(" + Add new ");
                    response(["+ Add new"]);
                }else {
                    console.log("Spinner hide")
                    response(data);
                }
                departmentSpinner.hide()
            }
        });
    },
    open: function(event, ui) {
        var menu = $(this).autocomplete("widget");
        var maxHeight = 200; // Set the maximum height for the suggestion box
        var itemCount = menu.children('li').length;
        menu.css('max-height', maxHeight + 'px');
        if (itemCount > 5) {
            menu.css({
                'overflow-y': 'auto',
                'overflow-x': 'hidden' // Hide the horizontal scrollbar
            });
        } else {
            menu.css('overflow', 'hidden');
        }
        menu.css( {
            "font-size" : "0.8rem",
            "border-radius": "0.25rem"
        });
        menu.find(".ui-menu-item").css( {
            "color" :"#6c757d"});
    },
    select: function(e, ui) {
        var departmentValue = (ui.item.value === "+ Add new") ? $('#department').val() : ui.item.value;
        $('#department').val(departmentValue);
        return false;
    }
});

$("#address").autocomplete({
    minLength: 2,
    source: function(request, response) {
        addressSpinner.css("top","44px");
        addressSpinner.show();
        console.log("Spinner loading..")
        $.ajax({
            url: "/fetch-address", // Replace with your server-side endpoint to fetch names
            data: {
                term: request.term
            },
            success: function(data) {
                if(data.length == 0) {
                    console.log(" + Add new ");
                    response(["+ Add new"]);
                }else {
                    console.log("Spinner hide")
                    response(data);
                }
                addressSpinner.hide()
            }
        });
    },
    open: function(event, ui) {
        var menu = $(this).autocomplete("widget");
        var maxHeight = 200; // Set the maximum height for the suggestion box
        var itemCount = menu.children('li').length;
        menu.css('max-height', maxHeight + 'px');
        if (itemCount > 5) {
            menu.css({
                'overflow-y': 'auto',
                'overflow-x': 'hidden' // Hide the horizontal scrollbar
            });
        } else {
            menu.css('overflow', 'hidden');
        }
        menu.css( {
            "font-size" : "0.8rem",
            "border-radius": "0.25rem"
        });
        menu.find(".ui-menu-item").css( {
            "color" :"#6c757d"});
    },
    select: function(e, ui) {
        var addressValue = (ui.item.value === "+ Add new") ? $('#address').val() : ui.item.value;
        $('#address').val(addressValue);
        return false;
    }
});
// Auto complete session end