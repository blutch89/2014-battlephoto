jQuery.centerThis = {
	build: function ()
	{
        jQuery.centerThis.images = this || {};
        jQuery.centerThis.center();

        return this;
	},

	center: function ()
	{
		var images = jQuery.centerThis.images;

		$.each(images, function(i, image)
		{
			var image = $(image);

			if (!image.is('img')) {
				console.error('CenterThis needs an image in order to work.');
				return false;
			}

			// Get container ratio
			var col = image.parent().parent();
			var colWidth = col.width();
			var colHeight = col.height();
			var colRatio = colWidth / colHeight;

			// Get image ratio
			var imgWidth = image.width();
			var imgHeight = image.height();
			var imgRatio = imgWidth / imgHeight;
			var imgRatioInv = imgHeight / imgWidth;
			var imgNewWidth = colHeight * imgRatio;
			var imgNewHeight = colWidth * imgRatioInv;
			
			// Fit
			if (colRatio + (colRatio * 0.05) >= imgRatio && colRatio - (colRatio * 0.05) <= imgRatio)
				image
					.css({
						'width': '100%',
						'height': '100%'
					});

			// Center on y
			else if (colRatio > imgRatio)
				image
					.css({
						'width': '100%',
						'height': 'auto',
						'margin-top': (colHeight - imgNewHeight) / 2 + 'px'
					});

			// Center on x
			else if (colRatio < imgRatio)
				image
					.css({
						'width': 'auto',
						'height': '100%',
						'margin-left': (colWidth - imgNewWidth) / 2 + 'px'
					});
		});
	}
};

jQuery.fn.extend(
	{
		centerThis : jQuery.centerThis.build
	}
);