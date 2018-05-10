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

curl 'http://localhost:3000/find-room-on-drawing?drawing-id=2&floor-id=1&version=C&coordsx=200&coordsy=280'
echo ""
curl 'http://localhost:3000/find-room-on-drawing?drawing-id=2&floor-id=1&version=C&coordsx=100&coordsy=280&x-offset=-100'
echo ""
curl 'http://localhost:3000/find-room-on-drawing?drawing-id=2&floor-id=1&version=C&coordsx=200&coordsy=180&y-offset=-100'
echo ""
curl 'http://localhost:3000/find-room-on-drawing?drawing-id=2&floor-id=1&version=C&coordsx=400&coordsy=450&scale=2'
echo ""
curl 'http://localhost:3000/find-room-on-drawing?drawing-id=2&floor-id=1&version=C&coordsx=400&coordsy=850&scale=2'
echo ""
