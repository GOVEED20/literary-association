import React from 'react'
import PaymentServiceFormField from './PaymentServiceFormField'

const PaymentServiceForm = ({ formFields,onChange }) => {
    return (
        <div>
            { formFields?.map((formField) =>
                < PaymentServiceFormField key={formField.name} formField={formField} onChange={onChange}/>
            )}
        </div>
    )
}

export default PaymentServiceForm