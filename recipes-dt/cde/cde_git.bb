SRC_URI = "git://git.code.sf.net/p/cdesktopenv/code file://Imake.cf file://linux-oe.cf file://host.def"
S = "${WORKDIR}/git/cde"
DEPENDS = "xproto-native imake-cross motif"
LICENSE = "LGPLv2"
SRCREV = "master"
LIC_FILES_CHKSUM = "file://COPYING;md5=5cad1229d150b7e335f29ee73fb4fd39"

export IMAKE = "imake"
PARALLEL_MAKE = ""

do_configure() {
    touch ${B}/config/imake/imake ${B}/config/imake/ccimake
    cp ${WORKDIR}/Imake.cf  ${B}/config/cf/
    cp ${WORKDIR}/linux-oe.cf  ${B}/config/cf/
    cp ${WORKDIR}/host.def  ${B}/config/cf/
    sed -e "s|@HOST_CC@|${CC}|g" \
        -e "s|@HOST_LD@|${LD}|g" \
        -e "s|@HOST_AS@|${AS}|g" \
        -e "s|@HOST_CXX@|${CXX}|g" \
        -e "s|@HOST_CPP@|${CPP}|g" \
        -i ${B}/config/cf/linux-oe.cf
}

do_compile() {
    rm -f ${B}/xmakefile
    oe_runmake IMAKE="${IMAKE}" CFLAGS="${CFLAGS} -I${STAGING_INCDIR}/X11" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" Makefile
    oe_runmake IMAKE="${IMAKE}" CFLAGS="${CFLAGS} -I${STAGING_INCDIR}/X11" BOOTSTRAPCFLAGS="-DUSE_CC_E -DCC_PROGRAM=\"${TARGET_SYS}\"-gcc" xmakefile
    unset CFLAGS
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile VerifyOS
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile Makefiles
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile Makefiles.doc
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile clean
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile clean.doc
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile includes
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile includes.doc
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile depend
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile depend.doc
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile all
    oe_runmake IMAKE="${IMAKE}" ONESUBDIR=tmp -f xmakefile all.doc
}

