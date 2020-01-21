RDEPENDS_${PN}_append = " \
	${@['', 'gpw-data-nfs'][d.getVar('WANTED_ROOT_DEV') == 'nfs']} \
"
