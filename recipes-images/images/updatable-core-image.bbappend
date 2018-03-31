# Copyright (C) 2018 Jens Rehsack <sno@netbsd.org>
# Released under the MIT license (see COPYING.MIT for the terms)

MOUNT_HELPER = "${@['', 'gpw-data-nfs'][d.getVar('WANTED_ROOT_DEV') == 'nfs']}"

RECOVER_INSTALL_append = "\
    ${MOUNT_HELPER} \
"
