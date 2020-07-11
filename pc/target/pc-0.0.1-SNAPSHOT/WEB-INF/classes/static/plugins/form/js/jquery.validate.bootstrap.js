(function($) {
$.extend($.validator.prototype, {
    showLabel: function(element, message) {
        var label = this.errorsFor( element );
        if (label.length == 0) {
            var railsGenerated = $(element).next('span.help-block');
            if (railsGenerated.length) {
                railsGenerated.attr('for', this.idOrName(element))
                railsGenerated.attr('generated', 'true');
                label = railsGenerated;
            }
        }
        if (label.length) {
            label.removeClass(this.settings.validClass).addClass(this.settings.errorClass);
            label.attr('generated') && label.html(message);
        } else {
            label = $('<' + this.settings.errorElement + '/>')
                  .attr({'for':  this.idOrName(element), generated: true})
                  .addClass(this.settings.errorClass)
                  .addClass('help-block')
                  .html(message || '');
            if (this.settings.wrapper) {
                label = label.hide().show().wrap('<' + this.settings.wrapper + '/>').parent();
            }
            if (!this.labelContainer.append(label).length) {
                this.settings.errorPlacement
                    ? this.settings.errorPlacement(label, $(element))
                    : label.insertAfter(element);
            }
        }
        if (!message && this.settings.success) {
            label.text('');
            typeof this.settings.success == 'string'
                ? label.addClass(this.settings.success)
                : this.settings.success(label);
        }
        this.toShow = this.toShow.add(label);
    }
});
$.extend($.validator.defaults, {
    errorClass: 'error',
    validClass: 'success',
    errorElement: 'span',
    highlight: function (element, errorClass, validClass) {
      $(element).closest('div.form-group').removeClass(validClass).addClass(errorClass);
    },
    unhighlight: function (element, errorClass, validClass) {
      $(element).closest('div.form-group').removeClass(errorClass).addClass(validClass);
      $(element).next('span.help-block').text('');
    },
    errorPlacement: function(error, element) {
    	if($(element).parent().hasClass('input-group')||$(element).parent().hasClass('radio-inline')||$(element).parent().hasClass('checkbox-inline')){
    		$(element).parent().parent().append(error);
    	}else{
    		$(element).parent().append(error);
    	}
    },
});
}(jQuery));