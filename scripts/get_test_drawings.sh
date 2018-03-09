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

wget -O drawing1.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C'
wget -O drawing2.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&selected=HOST.10.1S.05'
wget -O drawing3.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=occupation'
wget -O drawing4.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=occupation&selected=HOST.10.1S.05'
wget -O drawing5.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=room-type,occupation'
wget -O drawing6.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=capacity'
wget -O drawing7.png 'http://localhost:3000/raster-drawing?drawing-id=2&floor-id=1&version=C&highlight=capacity&selected=HOST.10.1S.05'

