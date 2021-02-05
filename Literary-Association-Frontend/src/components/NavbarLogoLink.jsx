import React from 'react'
import { Navbar as BootstrapNavbar } from 'react-bootstrap'
import { LinkContainer } from 'react-router-bootstrap'

const NavbarLogoLink = ({ imgPath, alt, url }) => (
    <LinkContainer to={url}>
        <BootstrapNavbar.Brand>
            <img src={imgPath} width='30' height='30' className="d-inline-block align-top" alt={alt}/>
        </BootstrapNavbar.Brand>
    </LinkContainer>
)

export default NavbarLogoLink