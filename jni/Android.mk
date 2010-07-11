#
#  Copyright (C) 2010 Manuel Luitz
#
#  This file is part of AndroHid.
#
#  This program is free software; you can redistribute it and/or modify it under
#  the terms of the GNU General Public License as published by the Free Software
#  Foundation; either version 3 of the License, or (at your option) any later
#  version.
# 
#  This program is distributed in the hope that it will be useful, but WITHOUT
#  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
#  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
#  details.
# 
#  You should have received a copy of the GNU General Public License along with
#  this program; If not, see <http://www.gnu.org/licenses/>.
#

LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := androhid
LOCAL_SRC_FILES :=  hidserv.c hidserv.h sdp.h sdp.c androhid.c

LOCAL_LDLIBS := -L$(SYSTEMROOT)/usr/lib -lbluetooth
LOCAL_DEFAULT_CPP_EXTENSION := cpp

#include $(BUILD_EXECUTABLE)
include $(BUILD_SHARED_LIBRARY)
