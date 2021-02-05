import React from 'react'
import { Nav, Navbar as BootstrapNavbar } from 'react-bootstrap'
import NavbarLink from './NavbarLink'
import NavbarLogoLink from './NavbarLogoLink'

const Navbar = ({ role }) => {
    switch (role) {
    case 'WRITER': {
        return (
            <BootstrapNavbar bg="dark" variant="dark">
                <NavbarLogoLink imgPath='/logo.svg' url='/dashboard/tasks' alt='Literary association logo'/>
                <Nav className="mr-auto">
                    <NavbarLink text='Tasks' url='/dashboard/tasks'/>
                    <NavbarLink text='My books' url='/dashboard/my-books'/>
                    <NavbarLink text='Membership' url='/dashboard/membership'/>
                </Nav>
                <Nav>
                    <NavbarLink text='Logout' url='/logout'/>
                </Nav>
            </BootstrapNavbar>
        )
    }
    case 'READER': {
        return (
            <BootstrapNavbar bg="dark" variant="dark">
                <NavbarLogoLink imgPath='/logo.svg' url='/dashboard/tasks' alt='Literary association logo'/>
                <Nav className="mr-auto">
                    <NavbarLink text='Tasks' url='/dashboard/tasks'/>
                    <NavbarLink text='Books' url='/dashboard/books'/>
                    <NavbarLink text='My books' url='/dashboard/my-books'/>
                </Nav>
                <Nav>
                    <NavbarLink text='Logout' url='/logout'/>
                </Nav>
            </BootstrapNavbar>
        )
    }
    case 'EDITOR':
    case 'LECTOR':
    case 'BOARD_MEMBER': {
        return (
            <BootstrapNavbar bg="dark" variant="dark">
                <NavbarLogoLink imgPath='/logo.svg' url='/dashboard/tasks' alt='Literary association logo'/>
                <Nav className="mr-auto">
                    <NavbarLink text='Tasks' url='/dashboard/tasks'/>
                </Nav>
                <Nav>
                    <NavbarLink text='Logout' url='/logout'/>
                </Nav>
            </BootstrapNavbar>
        )
    }
    default: {
        return null
    }
    }
}

export default Navbar
