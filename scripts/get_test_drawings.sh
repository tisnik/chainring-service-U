#!/bin/sh

#
#  (C) Copyright 2017, 2018  Pavel Tisnovsky
#
#  All rights reserved. This program and the accompanying materials
#  are made available under the terms of the Eclipse Public License v1.0
#  which accompanies this distribution, and is available at
#  http://www.eclipse.org/legal/epl-v10.html
#
#  Contributors:
#      Pavel Tisnovsky
#

wget -O drawing00.png 'http://localhost:3000/raster-drawing?drawing-id=1&floor-id=1&version=C'
wget -O drawing01.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C'
wget -O drawing02.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&selected=HOST.10.1S.03'
wget -O drawing03.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=occupation'
wget -O drawing04.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=occupation&selected=HOST.10.1S.03'
wget -O drawing05.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=room_type,occupation'
wget -O drawing06.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=room_type'
wget -O drawing07.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=capacity'
wget -O drawing08.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=capacity&selected=HOST.10.1S.03'
wget -O drawing09.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&scale=0.5'
wget -O drawing10.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&scale=2'
wget -O drawing11.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&x-offset=100'
wget -O drawing12.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&x-offset=-100'
wget -O drawing13.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&y-offset=100'
wget -O drawing14.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&y-offset=-100'
wget -O drawing15.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&x-offset=100&y-offset=100'
wget -O drawing16.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&x-offset=-100&y-offset=-100&scale=2'
wget -O drawing17.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&coordsx=200&coordsy=280'
wget -O drawing18.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&coordsx=100&coordsy=280&x-offset=-100'
wget -O drawing19.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&coordsx=200&coordsy=180&y-offset=-100'
wget -O drawing20.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&coordsx=400&coordsy=450&scale=2'
wget -O drawing21.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&debug=1&version=C&coordsx=200&coordsy=280'
wget -O drawing22.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&debug=1&version=C&coordsx=100&coordsy=280&x-offset=-100'
wget -O drawing23.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&debug=1&version=C&coordsx=200&coordsy=180&y-offset=-100'
wget -O drawing24.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&debug=1&version=C&coordsx=400&coordsy=450&scale=2'
