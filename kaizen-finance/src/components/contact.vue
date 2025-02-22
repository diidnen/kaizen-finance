<template>
  <div class="contact-page">
    <section class="contact-header">
      <h1>Contact Us</h1>
      <p>Get a Free Consultation</p>
    </section>

    <section class="contact-content" v-show="!formData.submit">
      <div class="contact-form-container">
        <form class="contact-form" @submit.prevent="handleSubmit">
          <div class="form-group">
            <input 
              type="text" 
              v-model="formData.name" 
              placeholder="Name*"
              @input="validateName"
            >
            <span v-if="!isValidName" class="error-message">Please enter a valid name</span>
          </div>
          
          <div class="form-group">
            <input 
              type="email" 
              v-model="formData.email" 
              placeholder="Email*"
              @input="validateEmail"
            >
            <span v-if="!isValidEmail" class="error-message">Please enter a valid email address</span>
          </div>

          <div class="form-group">
            <input 
              type="tel" 
              v-model="formData.phone" 
              placeholder="Phone Number (e.g. +44 7911 123456 or +1 234 567 8900)*"
              @input="validatePhone"
            >
            <span v-if="!isValidPhone" class="error-message">Please enter a valid international phone number</span>
          </div>

          <div class="form-group">
            <select v-model="formData.businessType">
              <option value="" disabled selected>Select Business Sector*</option>
              <option value="catering">Catering/Restaurant</option>
              <option value="beautician">Beautician</option>
              <option value="consultant">Consultant</option>
              <option value="charity">Charity</option>
            </select>
          </div>

          <div class="form-group">
            <select v-model="formData.turnover">
              <option value="" disabled selected>Annual Turnover*</option>
              <option value="0-50k">£0 - £50,000</option>
              <option value="50k-100k">£50,000 - £100,000</option>
              <option value="100k-250k">£100,000 - £250,000</option>
              <option value="250k-500k">£250,000 - £500,000</option>
              <option value="500k+">£500,000+</option>
            </select>
          </div>

          <div class="form-group">
            <input type="text" v-model="formData.countryOfTrade" placeholder="Country of Trade*" >
          </div>

          <div class="form-group">
            <input type="text" v-model="formData.fxCurrency" placeholder="Preferred FX Currency*" >
          </div>

          <div class="form-group dropdown-container">
            <button type="button" class="dropdown-button" @click="toggleServices">
              Other Services
              <span class="dropdown-arrow">▼</span>
            </button>
            <div class="dropdown-content" v-show="showServices">
              <div class="dropdown-item" 
                   v-for="service in availableServices" 
                   :key="service.value"
                   @click="toggleService(service.value)">
                <label>
                  <input type="checkbox" 
                         :value="service.value" 
                         v-model="formData.services">
                  {{ service.label }}
                </label>
              </div>
            </div>
            <div class="selected-services" v-if="formData.services.length > 0">
              <div class="selected-service" 
                   v-for="service in selectedServices" 
                   :key="service.value">
                {{ service.label }}
                <span class="remove-service" @click="removeService(service.value)">×</span>
              </div>
            </div>
          </div>

          <div class="form-group">
            <textarea 
              v-model="formData.additionalInfo" 
              :placeholder="getPlaceholderText()"
              rows="4"
            ></textarea>
          </div>

          <button 
            type="submit" 
            class="submit-btn" 
            :disabled="isSubmitting"
          >
            {{ isSubmitting ? 'SENDING...' : 'Submit' }}
          </button>
          
          <p class="privacy-notice">
            This site is protected by reCAPTCHA and the Google 
            <a href="https://policies.google.com/privacy" target="_blank">Privacy Policy</a> and 
            <a href="https://policies.google.com/terms" target="_blank">Terms of Service</a> apply.
          </p>
        </form>
      </div>

      <div class="contact-info">
        <div class="info-text">
          <p>
            We understand that each business has unique financial needs. Use our form to tell us 
            more about your requirements, and we will provide you with a free consultation for 
            our financial services.
          </p>
        </div>

        <div class="company-details">
          <h3>Kaizen Finance</h3>
          <p>London, UK</p>
          
          <h4>Hours</h4>
          <div class="business-hours">
            <p><strong>Monday - Sunday:</strong> 9:00 AM - 6:00 PM</p>
          </div>
        </div>
      </div>
    </section>

    <section class="contact-content" v-show="formData.submit">
      <h2>Thank you for your submission!</h2>
      <p>We will get back to you as soon as possible.</p>
    </section>
  </div>
</template>

<script>
import { http, auth } from '@/utils/request'

export default {
  name: 'Contact',
  data() {
    return {
      showServices: false,
      availableServices: [
        { value: 'vat', label: 'Need VAT Returns' },
        { value: 'payroll', label: 'Need Payroll Management' },
        { value: 'bookkeeping', label: 'Need Bookkeeping Services' },
        
      ],
      formData: {
        name: '',
        email: '',
        phone: '',
        businessType: '',
        turnover: '',
        vat: false,
        payroll: false,
        bookkeeping: false,
        additionalInfo: '',
        countryOfTrade: '',
        fxCurrency: '',
        submit: false,
        services: []
      },
      isSubmitting: false,
      isValidName: true,
      isValidEmail: true,
      isValidPhone: true
    }
  },
  computed: {
    selectedServices() {
      return this.availableServices.filter(service => 
        this.formData.services.includes(service.value)
      );
    }
  },
  methods: {
    validateEmail() {
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      this.isValidEmail = emailPattern.test(this.formData.email);
    },
    validateName() {
      if (this.formData.name.length > 0) {
        this.isValidName = true;
      } else {
        this.isValidName = false;
      }
    },
    validatePhone() {
      // 支持国际电话号码格式
      const phonePattern = /^\+?[1-9][0-9\s-()]{7,}$/;
      const cleanPhone = this.formData.phone.replace(/[\s()-]/g, '');
      this.isValidPhone = phonePattern.test(this.formData.phone) && cleanPhone.length >= 8 && cleanPhone.length <= 15;
    },
    async handleSubmit() {
      if (!this.isValidEmail || !this.isValidName || !this.isValidPhone || this.isSubmitting) {
        return;
      }
      
      try {
        this.isSubmitting = true;
        const response = await http.post('/send-email', this.formData);
        console.log(response);
        if (!response) {
          throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const result = response;
        console.log(result);
        if (result.success) {
          this.formData.submit = true;
        }
      } catch (error) {
        console.error('Error sending email:', error);
      } finally {
        this.isSubmitting = false;
      }
    },
    handleCountryChange() {
      if (this.formData.countryOfTrade === 'other') {
        this.updatePlaceholder();
      }
    },
    handleCurrencyChange() {
      if (this.formData.fxCurrency === 'other') {
        this.updatePlaceholder();
      }
    },
    getPlaceholderText() {
      let placeholder = 'Any other questions or special requirements?';
      
      if (this.formData.countryOfTrade === 'other') {
        placeholder += '\nPlease specify your country of trade.';
      }
      
      if (this.formData.fxCurrency === 'other') {
        placeholder += '\nPlease specify your preferred currency.';
      }
      
      return placeholder;
    },
    updatePlaceholder() {
      const textarea = document.querySelector('textarea');
      if (textarea) {
        textarea.placeholder = this.getPlaceholderText();
      }
    },
    toggleServices() {
      this.showServices = !this.showServices;
    },
    toggleService(value) {
      const index = this.formData.services.indexOf(value);
      if (index === -1) {
        this.formData.services.push(value);
      } else {
        this.formData.services.splice(index, 1);
      }
    },
    removeService(value) {
      const index = this.formData.services.indexOf(value);
      if (index !== -1) {
        this.formData.services.splice(index, 1);
      }
    }
  }
}
</script>

<style scoped>
.contact-page {
  padding-top: 1rem;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  min-height: 100vh;
}

.contact-header {
  text-align: center;
  padding: 2rem 1rem;
}

.contact-header h1 {
  font-size: 2.4rem;
  color: #2d3436;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.contact-header p {
  font-size: 1.2rem;
  color: #636e72;
}

.contact-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 1rem;
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 2rem;
}

.contact-form-container {
  background: white;
  padding: 2rem;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
}

.form-group {
  margin-bottom: 1.2rem;
}

input, select, textarea {
  width: 100%;
  padding: 0.8rem;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
  background: #f8f9fa;
}

input:focus, select:focus, textarea:focus {
  outline: none;
  border-color: #007bff;
  background: white;
  box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.1);
}

select {
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' fill='%23333' viewBox='0 0 16 16'%3E%3Cpath d='M8 11L3 6h10l-5 5z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  padding-right: 2.5rem;
}

textarea {
  min-height: 100px;
}

.services-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1rem;
  margin: 1.5rem 0;
}

.service-option {
  background: #f8f9fa;
  padding: 0.8rem;
  border-radius: 8px;
  border: 2px solid #e9ecef;
}

.service-option:hover {
  border-color: #007bff;
  transform: translateY(-2px);
}

.service-option label {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  cursor: pointer;
  font-weight: 500;
  color: #2d3436;
}

.service-option input[type="checkbox"] {
  width: 20px;
  height: 20px;
  border: 2px solid #007bff;
  border-radius: 6px;
  margin: 0;
}

.submit-btn {
  width: 100%;
  padding: 0.8rem;
  background: linear-gradient(135deg, #007bff 0%, #0056b3 100%);
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 600;
  letter-spacing: 0.5px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 1rem;
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0, 123, 255, 0.2);
}

.submit-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 123, 255, 0.3);
  background: linear-gradient(135deg, #0056b3 0%, #004494 100%);
}

.submit-btn:active {
  transform: translateY(0);
  box-shadow: 0 2px 10px rgba(0, 123, 255, 0.2);
}

.submit-btn:disabled {
  background: linear-gradient(135deg, #c8d6e5 0%, #b2bec3 100%);
  cursor: not-allowed;
  transform: none;
  box-shadow: none;
  opacity: 0.7;
}

.error-message {
  color: #ff6b6b;
  font-size: 0.9rem;
  margin-top: 0.5rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.error-message::before {
  content: "!";
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
  background: #ff6b6b;
  color: white;
  border-radius: 50%;
  font-size: 0.8rem;
  font-weight: bold;
}

.privacy-notice {
  margin-top: 1rem;
  font-size: 0.9rem;
  color: #636e72;
  text-align: center;
  line-height: 1.6;
}

.privacy-notice a {
  color: #007bff;
  text-decoration: none;
  font-weight: 500;
  transition: color 0.3s ease;
}

.privacy-notice a:hover {
  color: #0056b3;
  text-decoration: underline;
}

.contact-info {
  padding: 1rem;
}

.info-text {
  margin-bottom: 1.5rem;
}

.company-details {
  padding: 1.5rem;
}

.company-details h3 {
  font-size: 1.8rem;
  margin-bottom: 1rem;
  color: #2d3436;
}

.company-details h4 {
  font-size: 1.3rem;
  margin: 1.5rem 0 1rem;
  color: #2d3436;
}

.business-hours p {
  margin-bottom: 0.8rem;
  color: #636e72;
}

@media (max-width: 768px) {
  .contact-content {
    grid-template-columns: 1fr;
    gap: 1.5rem;
    padding: 1rem;
  }

  .contact-form-container {
    padding: 1.2rem;
  }

  .contact-header {
    padding: 1.5rem 1rem;
  }
}

.dropdown-container {
  position: relative;
}

.dropdown-button {
  width: 100%;
  padding: 0.8rem;
  background: #f8f9fa;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  text-align: left;
  cursor: pointer;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dropdown-button:hover {
  border-color: #007bff;
}

.dropdown-arrow {
  font-size: 0.8rem;
}

.dropdown-content {
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  background: white;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  margin-top: 4px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  z-index: 1000;
}

.dropdown-item {
  padding: 0.8rem;
  cursor: pointer;
}

.dropdown-item:hover {
  background: #f8f9fa;
}

.dropdown-item label {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  cursor: pointer;
}

.dropdown-item input[type="checkbox"] {
  width: 18px;
  height: 18px;
}

.selected-services {
  margin-top: 0.5rem;
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.selected-service {
  background: #e9ecef;
  padding: 0.4rem 0.8rem;
  border-radius: 4px;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.remove-service {
  cursor: pointer;
  font-weight: bold;
  color: #666;
  padding: 0 0.2rem;
}

.remove-service:hover {
  color: #dc3545;
}
</style>
