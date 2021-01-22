import React from 'react'
import { Nav, Navbar as BootstrapNavbar } from 'react-bootstrap'
import NavbarLink from './NavbarLink'
import NavbarLogoLink from './NavbarLogoLink'

const Navbar = ({ role }) => {
    // will be hard coded for writer for now
    switch (role) {
    case 'WRITER': {
        return (
            <BootstrapNavbar bg="dark" variant="dark">
                <NavbarLogoLink imgPath='/logo.svg' url='/dashboard/tasks' alt='Literary association logo'/>
                <Nav className="mr-auto">
                    <NavbarLink text='Tasks' url='/dashboard/tasks'/>
                    <NavbarLink text='Books' url='/dashboard/books'/>
                    <NavbarLink text='My books' url='/dashboard/my-books'/>
                    <NavbarLink text='Profile' url='/dashboard/profile'/>
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