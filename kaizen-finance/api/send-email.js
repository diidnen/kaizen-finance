import { submitContact } from '../lib/submitContact.js'

export default async function handler(req, res) {
  if (req.method !== 'POST') {
    return res.status(405).json({ success: false, message: 'Method not allowed' })
  }

  try {
    const body = typeof req.body === 'string' ? JSON.parse(req.body) : req.body
    await submitContact(body, process.env.WEB3FORMS_ACCESS_KEY)
    return res.status(200).json({ success: true })
  } catch (error) {
    console.error('Contact form error:', error)
    const message = error.message || 'Failed to send enquiry'
    const status = message.includes('not configured') ? 503 : 500
    return res.status(status).json({ success: false, message })
  }
}
