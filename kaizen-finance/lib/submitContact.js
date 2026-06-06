function buildMessage(data) {
  const lines = [
    `Name: ${data.name}`,
    `Email: ${data.email}`,
    data.phone && `Phone: ${data.phone}`,
    data.businessType && `Business Sector: ${data.businessType}`,
    data.turnover && `Annual Turnover: ${data.turnover}`,
    data.countryOfTrade && `Country of Trade: ${data.countryOfTrade}`,
    data.fxCurrency && `Preferred FX Currency: ${data.fxCurrency}`,
    data.serviceLabels && `Services: ${data.serviceLabels}`,
    data.otherServicesText && `Other Services: ${data.otherServicesText}`,
    data.additionalInfo && `Additional Info: ${data.additionalInfo}`,
  ].filter(Boolean)

  return lines.join('\n')
}

export async function submitContact(data, accessKey) {
  if (!accessKey) {
    throw new Error('Contact form is not configured. Please set VITE_WEB3FORMS_ACCESS_KEY.')
  }

  const response = await fetch('https://api.web3forms.com/submit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    body: JSON.stringify({
      access_key: accessKey,
      subject: `New enquiry from ${data.name}`,
      from_name: data.name,
      email: data.email,
      phone: data.phone || '',
      business_type: data.businessType || '',
      turnover: data.turnover || '',
      country_of_trade: data.countryOfTrade || '',
      fx_currency: data.fxCurrency || '',
      services: data.serviceLabels || '',
      message: buildMessage(data),
    }),
  })

  const contentType = response.headers.get('content-type') || ''
  if (!contentType.includes('application/json')) {
    throw new Error('Failed to send enquiry. Please email us at Info@kaizensolution.co.uk')
  }

  const result = await response.json()

  if (!response.ok || !result.success) {
    throw new Error(result.message || 'Failed to send enquiry')
  }

  return { success: true }
}
