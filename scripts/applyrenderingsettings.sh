#!/bin/bash

omero="/opt/omero/server/OMERO.server/bin/omero"
$omero login

cd ../experimentA/renderingsettings

IFS=$(echo -en "\n\b")
for f in *.json
do
	imgname=${f%.*}

	echo "Set rendering settings for $imgname"
	
	imgid=`$omero hql --style csv -q "select img.id from Image img where img.name = '$imgname'"`
	imgid=${imgid##*,}
	
	$omero render set Image:$imgid $f
done
