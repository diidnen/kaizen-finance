<template>
  <div class="pricing-container"> 
    <h1 class="main-title">Service Price List 2024</h1>
    
    <!-- Service Categories -->
    <div class="service-categories">
      <button 
        v-for="(category, index) in categories" 
        :key="index"
        :class="['category-btn', { active: currentCategory === index }]"
        @click="currentCategory = index"
      >
        {{ category.name }}
      </button>
    </div>

    <!-- Service Selection -->
    <div class="services-list">
      <div v-for="service in currentServices" :key="service.id" class="service-item">
        <div class="service-header">
          <h3 class="service-name">{{ service.name }}</h3>
          <div class="service-description" v-if="service.description">
            {{ service.description }}
          </div>
        </div>

        <div class="service-pricing">
          <!-- Price Option Container -->
          <div v-if="service.hasMonthly || service.hasYearly || service.hasFixed">
            <!-- Date Picker (shown only for consultancy services) -->
            <consultancy-date-picker
              :service-id="service.id"
              @change="handleTimeChange"
            />

            <!-- Monthly Pricing -->
            <div v-if="service.hasMonthly" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'monthly'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <span class="price-label">Per Month (Exc VAT)</span>
                  <span class="price-amount">£{{ service.monthlyPrice.toFixed(2) }}</span>
                  <span class="price-label">Per Month (Inc VAT)</span>
                  <span class="price-amount">£{{ (service.monthlyPrice * (1 + service.vatRate)).toFixed(2) }}</span>
                </div>
              </label>
            </div>

            <!-- Yearly Pricing -->
            <div v-if="service.hasYearly" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'yearly'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <span class="price-label">Per Year (Exc VAT)</span>
                  <span class="price-amount">£{{ service.yearlyPrice.toFixed(2) }}</span>
                  <span class="price-label">Per Year (Inc VAT)</span>
                  <span class="price-amount">£{{ (service.yearlyPrice * (1 + service.vatRate)).toFixed(2) }}</span>
                </div>
              </label>
            </div>

            <!-- Fixed Price -->
            <div v-if="service.hasFixed" class="price-option">
              <label class="price-checkbox">
                <input 
                  type="checkbox" 
                  v-model="selectedServices" 
                  :value="{id: service.id, type: 'fixed'}"
                  @change="updateQuote"
                >
                <div class="price-details">
                  <span class="price-label">Fixed Price (Exc VAT)</span>
                  <span class="price-amount">
                    £{{ service.fixedPrice.toFixed(2) }}
                    <span v-if="service.priceNote" class="price-note">{{ service.priceNote }}</span>
                  </span>
                  <span class="price-label">Fixed Price (Inc VAT)</span>
                  <span class="price-amount">£{{ (service.fixedPrice * (1 + service.vatRate)).toFixed(2) }}</span>
                </div>
              </label>
            </div>
          </div>

          <!-- Enquiry Only -->
          <div v-if="service.isEnquiryOnly" class="enquiry-option">
            <label class="price-checkbox">
              <input 
                type="checkbox" 
                v-model="selectedServices" 
                :value="{id: service.id, type: 'enquiry'}"
                @change="sendEnquiry(service)"
              >
              <div class="price-details">
                <span class="enquiry-text">{{ service.enquiryText }}</span>
              </div>
            </label>
          </div>
        </div>
      </div>
    </div>

    <!-- Quote Summary -->
    <div v-if="selectedServices.length > 0" class="quote-summary">
      <h2>Your Quote Summary</h2>
      
      <!-- 有价格的服务 -->
      <div v-if="pricedServices.length > 0" class="summary-table">
        <div class="summary-header">
          <span>Service</span>
          <span>Type</span>
          <span>Time</span>
          <span>Exc VAT</span>
          <span>VAT</span>
          <span>Inc VAT</span>
        </div>
        <div v-for="item in pricedServices" :key="`${item.id}-${item.type}`" class="summary-row">
          <span class="service-name">{{ item.name }}</span>
          <span class="service-type">{{ item.type }}</span>
          <span class="service-time">{{ item.time || '-' }}</span>
          <span class="price">£{{ item.priceExVat.toFixed(2) }}</span>
          <span class="vat">£{{ item.vat.toFixed(2) }}</span>
          <span class="total">£{{ item.priceIncVat.toFixed(2) }}</span>
        </div>
        <div class="summary-total">
          <span>Total</span>
          <span></span>
          <span></span>
          <span>£{{ totals.exVat.toFixed(2) }}</span>
          <span>£{{ totals.vat.toFixed(2) }}</span>
          <span>£{{ totals.incVat.toFixed(2) }}</span>
        </div>
      </div>

      <!-- 询价服务 -->
      <div v-if="enquiryServices.length > 0" class="enquiry-summary">
        <h3>Services Requiring Enquiry</h3>
        <div v-for="item in enquiryServices" :key="item.id" class="enquiry-row">
          <span class="service-name">{{ item.name }}</span>
          <div class="enquiry-details">
            <span class="enquiry-status">{{ item.enquiryText }}</span>
            <span v-if="item.time" class="enquiry-time">
              Scheduled: {{ item.time }}
            </span>
          </div>
        </div>
      </div>

      <!-- Special Notes -->
      <div class="special-notes">
        <h3>Important Notes</h3>
        <ul>
          <li v-for="(note, index) in notes" :key="index">{{ note }}</li>
        </ul>
      </div>

      <!-- Contact Information -->
      <div class="contact-info">
        <h3>Contact Us to Confirm Your Quote</h3>
        <div class="contact-details">
          <p><strong>Contact:</strong> Zhibing Mao</p>
          <p><strong>Tel:</strong> <a href="tel:07808850046">078 088 500 46</a></p>
          <p><strong>Email:</strong> <a href="mailto:info@kaizensolution.co.uk">info@kaizensolution.co.uk</a></p>
        </div>
      </div>

      <!-- Action Buttons -->
      <div class="action-buttons">
        <button class="generate-pdf-btn" @click="generatePDF">
          <i class="fas fa-file-pdf"></i>
          Generate Quote PDF
        </button>
        <button class="send-enquiry-btn" @click="submitQuote">
          <i class="fas fa-envelope"></i>
          Submit Quote
        </button>
      </div>
    </div>

    <!-- Add submit button -->
    
  </div>
</template>

<script>
import { jsPDF } from 'jspdf'
import 'jspdf-autotable'
import { ref } from 'vue'
import { http, auth } from '@/utils/request'
import { ElMessage } from 'element-plus'
import ConsultancyDatePicker from './ConsultancyDatePicker.vue'

export default {
  name: 'Pricing',
  components: {
    ConsultancyDatePicker
  },
  data() {
    return {
      currentCategory: 0,
      selectedServices: ref([]),
      enquiryServices: ref([]),
      consultancyTimes: {},
      categories: [
        {
          name: 'START-UP AND PACKAGE OFFER',
          services: [
            {
              id: 'bp1',
              name: 'Business Plan (Basic)',
              description: 'Start-up SME service',
              hasFixed: true,
              fixedPrice: 1500,
              yearlyPrice: 1800,
              vatRate: 0.20
            },
            {
              id: 'setup1',
              name: 'Set-up of Company (Basic)',
              description: 'Start-up SME service',
              hasFixed: true,
              fixedPrice: 250,
              yearlyPrice: 300,
              vatRate: 0.20
            },
            {
              id: 'annual1',
              name: 'Annual Account Service Package',
              description: 'Start-up small business first year offer',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 125,
              monthlyPriceIncVat: 150,
              yearlyPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            },
            {
              id: 'bookkeeping1',
              name: 'Book-keeping Fix Cost Offer',
              description: 'Start-up small business first year offer',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 104,
              monthlyPriceIncVat: 125,
              yearlyPrice: 1250,
              yearlyPriceIncVat: 1500,
              vatRate: 0.20
            },
            {
              id: 'webdesign1',
              name: 'Web Page Design',
              description: 'Start-up small business first year offer',
              hasFixed: true,
              fixedPrice: 1900,
              yearlyPriceIncVat: 2280,
              vatRate: 0.20
            },
            {
              id: 'paperwork1',
              name: 'General Paper Work Review',
              description: '(+3rd party Communication fee 10% Disc)',
              hasFixed: true,
              fixedPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'FINANCIAL SERVICE',
          services: [
            {
              id: 'annual-sole',
              name: 'Annual Account - Sole Trader',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 75,
              monthlyPriceIncVat: 90,
              yearlyPrice: 900,
              yearlyPriceIncVat: 1080,
              vatRate: 0.20
            },
            {
              id: 'annual-small',
              name: 'Annual Account - Company <£61K turn over',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 125,
              monthlyPriceIncVat: 150,
              yearlyPrice: 1500,
              yearlyPriceIncVat: 1800,
              vatRate: 0.20
            },
            {
              id: 'annual-large',
              name: 'Annual Account - Company >£61K turn over',
              description: 'from',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 150,
              monthlyPriceIncVat: 180,
              yearlyPrice: 1800,
              yearlyPriceIncVat: 2160,
              vatRate: 0.20
            },
            {
              id: 'tax-return',
              name: 'Tax Return',
              hasFixed: true,
              fixedPrice: 295,
              priceNote: 'per return',
              vatRate: 0.20
            },
            {
              id: 'vat-return',
              name: 'VAT Return',
              hasFixed: true,
              fixedPrice: 425,
              priceNote: 'per return',
              vatRate: 0.20
            },
            {
              id: 'closing',
              name: 'Closing Company',
              description: '(excl account & third party fees)',
              hasFixed: true,
              fixedPrice: 485,
              priceNote: 'from',
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'PAYROLL',
          services: [
            {
              id: 'payroll-employee',
              name: 'Per Employees',
              hasMonthly: true,
              hasYearly: true,
              monthlyPrice: 24,
              yearlyPrice: 285,
              priceNote: 'from',
              vatRate: 0.20
            },
            {
              id: 'pension-plan',
              name: 'Pension Plan Per Employee',
              description: '(exclude External cost)',
              hasFixed: true,
              fixedPrice: 485,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'BOOK-KEEPING',
          services: [
            {
              id: 'bookkeeping-35',
              name: 'Up to 35 Invoice',
              hasFixed: true,
              fixedPrice: 350,
              vatRate: 0.20
            },
            {
              id: 'bookkeeping-70',
              name: 'Up to 70 Invoice',
              hasFixed: true,
              fixedPrice: 490,
              vatRate: 0.20
            },
            {
              id: 'accounting-service',
              name: 'Accounting Service',
              description: '(e.g. Credit controls, cash flow forecast, management information (MI) and MI system development)',
              isEnquiryOnly: true,
              enquiryText: 'Please ask for price'
            }
          ]
        },
        {
          name: 'CONSULTANCY & COMMUNICATIONS',
          services: [
            {
              id: 'consult-offsite',
              name: 'Consultancy Fee Per Session (30 mins) - Off Site',
              hasFixed: true,
              fixedPrice: 250,
              vatRate: 0.20
            },
            {
              id: 'consult-onsite',
              name: 'Consultancy Fee - On Client Site',
              description: '(min 1 hour)',
              hasFixed: true,
              fixedPrice: 295,
              vatRate: 0.20
            },
            {
              id: 'comm-letter',
              name: 'Letter per page/email',
              hasFixed: true,
              fixedPrice: 55,
              vatRate: 0.20
            },
            {
              id: 'comm-call-local',
              name: 'Telephone call per min (Local)',
              hasFixed: true,
              fixedPrice: 5,
              vatRate: 0.20
            },
            {
              id: 'comm-call-intl',
              name: 'Telephone call per min (International)',
              hasFixed: true,
              fixedPrice: 7.50,
              vatRate: 0.20
            },
            {
              id: 'comm-copy',
              name: 'Photo copy',
              hasFixed: true,
              fixedPrice: 5,
              vatRate: 0.20
            },
            {
              id: 'comm-post',
              name: 'Postage service',
              hasFixed: true,
              fixedPrice: 7,
              vatRate: 0.20
            }
          ]
        },
        {
          name: 'OTHER SERVICES',
          services: [
            {
              id: 'virtual-office',
              name: 'Virtual Office',
              hasMonthly: true,
              monthlyPrice: 132,
              vatRate: 0.20
            },
            {
              id: 'virtual-secretary',
              name: 'Virtual Secretary',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'marketing',
              name: 'Marketing and Web page design',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'it-solution',
              name: 'IT Solution',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'business-dev',
              name: 'Business Development & Consultancy',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'training',
              name: 'Training - Computing and Finance',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'legal',
              name: 'Legal (referral)',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'property-finance',
              name: 'Property and Business financing',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            },
            {
              id: 'fx-transfer',
              name: 'FX transfer',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire'
            }
          ]
        },
        {
          name: 'VIRTUAL SECRETARY & OTHERS',
          services: [
            {
              id: 'virtual-secretary',
              name: 'Virtual Secretary Service',
              description: 'Professional virtual assistance and administrative support',
              isEnquiryOnly: true,
              enquiryText: 'Please enquire for pricing'
            },
            {
              id: 'credit-control',
              name: 'Credit Controls',
              description: 'Cash flow forecast, management information (MI) and MI system development',
              isEnquiryOnly: true,
              enquiryText: 'Please ask for price'
            },
            {
              id: 'consultancy',
              name: 'Consultancy Service',
              description: 'Off Site: £250 (Exc VAT) / £300 (Inc VAT)\nOn Client Site: £295 (Exc VAT) / £354 (Inc VAT) (min 1 hour)',
              isEnquiryOnly: true,
              enquiryText: 'Per session (30 mins)'
            }
          ]
        }
      ],
      notes: [
        '* 15% discount if Book-keeping service have been taken',
        '** All members will receive one free consultancy session',
        '^ 5 minutes will be charge the full sessions. If on-site, cost and traveling time will be added'
      ]
    }
  },
  computed: {
    currentServices() {
      return this.categories[this.currentCategory].services
    },
    selectedServiceDetails() {
      return this.selectedServices.map(item => {
        const service = this.findService(item.id)
        let priceExVat = 0, vat = 0, priceIncVat = 0

        if (!service.isEnquiryOnly) {
          switch(item.type) {
            case 'monthly':
              priceExVat = service.monthlyPrice
              break
            case 'yearly':
              priceExVat = service.yearlyPrice
              break
            case 'fixed':
              priceExVat = service.fixedPrice
              break
          }
          vat = priceExVat * service.vatRate
          priceIncVat = priceExVat * (1 + service.vatRate)
        }

        return {
          id: service.id,
          name: service.name,
          type: item.type,
          priceExVat,
          vat,
          priceIncVat,
          isEnquiryOnly: service.isEnquiryOnly,
          enquiryText: service.enquiryText,
          time: this.consultancyTimes[service.id]
        }
      })
    },
    pricedServices() {
      return this.selectedServiceDetails.filter(s => !s.isEnquiryOnly)
    },
    enquiryServices() {
      return this.selectedServiceDetails.filter(s => s.isEnquiryOnly)
    },
    totals() {
      return this.pricedServices.reduce((acc, item) => ({
        exVat: acc.exVat + item.priceExVat,
        vat: acc.vat + item.vat,
        incVat: acc.incVat + item.priceIncVat
      }), { exVat: 0, vat: 0, incVat: 0 })
    }
  },
  methods: {
    findService(id) {
      for (const category of this.categories) {
        const service = category.services.find(s => s.id === id)
        if (service) return service
      }
      return null
    },
    updateQuote() {
      // Handle quote updates
    },
    async generatePDF() {
      const doc = new jsPDF()
      
      // 添加标题
      doc.setFontSize(20)
      doc.text('Kaizen Solution - Service Quote', 105, 20, { align: 'center' })
      
      // 添加日期
      doc.setFontSize(12)
      doc.text(`Date: ${new Date().toLocaleDateString()}`, 14, 30)
      
      let currentY = 40
      
      // 分开处理有价格和询价的服务
      const pricedServices = this.selectedServiceDetails.filter(s => !s.isEnquiryOnly)
      const enquiryServices = this.selectedServiceDetails.filter(s => s.isEnquiryOnly)
      
      // 添加有价格的服务表格
      if (pricedServices.length > 0) {
        doc.text('Services with Fixed Pricing:', 14, currentY)
        const tableData = pricedServices.map(item => [
          item.name,
          item.type,
          item.time || '-',
          `£${item.priceExVat.toFixed(2)}`,
          `£${item.vat.toFixed(2)}`,
          `£${item.priceIncVat.toFixed(2)}`
        ])
        
        doc.autoTable({
          startY: currentY + 10,
          head: [['Service', 'Type', 'Time', 'Price (Exc VAT)', 'VAT', 'Price (Inc VAT)']],
          body: tableData,
          foot: [[
            'Total',
            '',
            '',
            `£${this.totals.exVat.toFixed(2)}`,
            `£${this.totals.vat.toFixed(2)}`,
            `£${this.totals.incVat.toFixed(2)}`
          ]],
          styles: {
            fontSize: 10
          },
          columnStyles: {
            0: { cellWidth: 50 }, // Service name
            1: { cellWidth: 20 }, // Type
            2: { cellWidth: 35 }, // Time
            3: { cellWidth: 25 }, // Price Exc VAT
            4: { cellWidth: 25 }, // VAT
            5: { cellWidth: 25 }  // Price Inc VAT
          }
        })
        
        currentY = doc.lastAutoTable.finalY + 20
      }
      
      // 添加询价服务列表
      if (enquiryServices.length > 0) {
        doc.text('Services Requiring Enquiry:', 14, currentY)
        currentY += 10
        
        enquiryServices.forEach(service => {
          doc.text(`• ${service.name}`, 20, currentY)
          if (service.time) {
            currentY += 7
            doc.setFontSize(10)
            doc.text(`Scheduled Time: ${service.time}`, 25, currentY)
            doc.setFontSize(12)
          }
          if (service.description) {
            currentY += 7
            doc.setFontSize(10)
            doc.text(service.description, 25, currentY)
            doc.setFontSize(12)
          }
          currentY += 10
        })
        
        currentY += 10
      }
      
      // 添加注意事项
      doc.text('Important Notes:', 14, currentY)
      currentY += 10
      this.notes.forEach(note => {
        doc.text(note, 14, currentY)
        currentY += 10
      })
      
      // 添加联系信息
      currentY += 10
      doc.text('Contact Information:', 14, currentY)
      currentY += 10
      doc.text('Zhibing Mao', 14, currentY)
      currentY += 10
      doc.text('Tel: 078 088 500 46', 14, currentY)
      currentY += 10
      doc.text('Email: info@kaizensolution.co.uk', 14, currentY)
      
      // 保存 PDF
      doc.save('kaizen-solution-quote.pdf')
    },
    async sendEnquiry(service) {
      try {
        const username = auth.getUsername()
        if (!username) {
          ElMessage.error('Please login first')
          return
        }

        // 检查服务是否已经在 selectedServices 中
        const existingIndex = this.selectedServices.findIndex(s => s.id === service.id)
        if (existingIndex === -1) {
          // 如果不在，添加到 selectedServices
          this.selectedServices.push({
            id: service.id,
            type: 'enquiry',
            time: service.id.includes('consult') ? null : undefined // 只为咨询服务初始化时间
          })
        }

        ElMessage.success('Service added to enquiry list')
        this.updateQuote()
      } catch (error) {
        console.error('Error adding enquiry service:', error)
        ElMessage.error('Failed to add service to enquiry list: ' + error.message)
      }
    },
    handleTimeChange(serviceId, time) {
      // 更新咨询时间
      this.consultancyTimes[serviceId] = time
      
      // 更新选中服务的时间
      const serviceIndex = this.selectedServices.findIndex(s => s.id === serviceId)
      if (serviceIndex !== -1) {
        this.selectedServices[serviceIndex] = {
          ...this.selectedServices[serviceIndex],
          time: time
        }
      }
      
      this.updateQuote()
    },
    async submitQuote() {
      try {
        const username = auth.getUsername()
        if (!username) {
          ElMessage.error('Please login first')
          return
        }

        // 构造所有服务数据，包括询价服务
        const services = this.selectedServiceDetails.map(service => ({
          name: service.name,
          price: service.isEnquiryOnly ? 'Enquiry Required' : service.priceIncVat.toFixed(2),
          type: service.type,
          time: service.time || undefined
        }))

        const enquiryServices = this.enquiryServices.map(service => ({
          id: service.id,
          name: service.name,
          description: service.description,
          price: service.enquiryText,
          type: 'Enquiry',
          time: service.time || undefined
        }))

        const response = await http.post('/submit-order', {
          username: username,
          services: services,
          enquiryServices: enquiryServices,
          orderDate: new Date().toISOString()
        })

        

        if (response.code === 200) {
          ElMessage.success('Quote submitted successfully')
          // 先导航到order页面
          await this.$router.push('/order')
          // 等待导航完成后再刷新
          setTimeout(() => {
            window.location.reload()
          }, 100)
        } else {
          ElMessage.error('Failed to submit quote')
        }
      } catch (error) {
        console.error('Error submitting quote:', error)
        ElMessage.error('Failed to submit quote: ' + error.message)
      }
    }
  }
}
</script>

<style scoped>
.pricing-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: Arial, sans-serif;
}

.main-title {
  color: #1B365D;
  text-align: center;
  margin-bottom: 2rem;
  font-size: 2.5rem;
}

/* Category Navigation */
.service-categories {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  margin-bottom: 2rem;
}

.category-btn {
  padding: 1rem 1.5rem;
  background: #f8f9fa;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 600;
  color: #1B365D;
}

.category-btn.active {
  background: #1B365D;
  color: white;
}

/* Service Items */
.services-list {
  display: grid;
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.service-item {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.service-header {
  margin-bottom: 1rem;
}

.service-name {
  font-size: 1.2rem;
  color: #1B365D;
  margin: 0;
  font-weight: 600;
}

.service-description {
  color: #666;
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

/* Pricing Options */
.service-pricing {
  display: grid;
  gap: 1rem;
}

.price-option {
  background: #f8f9fa;
  padding: 1rem;
  border-radius: 4px;
}

.price-checkbox {
  display: flex;
  align-items: center;
  gap: 1rem;
  cursor: pointer;
}

.price-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.5rem;
  flex-grow: 1;
}

.price-label {
  color: #666;
  font-size: 0.9rem;
}

.price-amount {
  font-weight: 600;
  color: #1B365D;
}

.price-note {
  font-size: 0.8rem;
  color: #666;
  margin-left: 0.5rem;
}

/* Enquiry Option */
.enquiry-option {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.enquiry-text {
  color: #1B365D;
  font-style: italic;
}

.enquiry-btn {
  padding: 0.5rem 1rem;
  background: transparent;
  border: 1px solid #1B365D;
  color: #1B365D;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.enquiry-btn:hover {
  background: #1B365D;
  color: white;
}

/* Quote Summary */
.quote-summary {
  background: white;
  padding: 2rem;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  margin-top: 2rem;
}

.summary-table {
  width: 100%;
  margin: 1.5rem 0;
}

.summary-header {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1fr 1fr 1fr;
  gap: 1rem;
  padding: 1rem;
  background: #f8f9fa;
  font-weight: 600;
  color: #1B365D;
}

.summary-row {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1fr 1fr 1fr;
  gap: 1rem;
  padding: 1rem;
  border-bottom: 1px solid #eee;
}

.summary-total {
  display: grid;
  grid-template-columns: 2fr 1fr 1.5fr 1fr 1fr 1fr;
  gap: 1rem;
  padding: 1rem;
  font-weight: 600;
  background: #f8f9fa;
  color: #1B365D;
}

/* Special Notes */
.special-notes {
  margin: 2rem 0;
  padding: 1.5rem;
  background: #f8f9fa;
  border-radius: 4px;
  border-left: 4px solid #1B365D;
}

.special-notes h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

.special-notes ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.special-notes li {
  margin: 0.5rem 0;
  color: #666;
}

/* Contact Information */
.contact-info {
  margin: 2rem 0;
  padding: 1.5rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.contact-info h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

.contact-details {
  display: grid;
  gap: 0.5rem;
}

.contact-details a {
  color: #1B365D;
  text-decoration: none;
}

.contact-details a:hover {
  text-decoration: underline;
}

/* Action Buttons */
.action-buttons {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-top: 2rem;
}

.generate-pdf-btn,
.send-enquiry-btn {
  padding: 1rem;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.generate-pdf-btn {
  background: #1B365D;
  color: white;
}

.send-enquiry-btn {
  background: white;
  color: #1B365D;
  border: 2px solid #1B365D;
}

.generate-pdf-btn:hover {
  background: #2C5282;
}

.send-enquiry-btn:hover {
  background: #1B365D;
  color: white;
}

/* Responsive Design */
@media (max-width: 768px) {
  .pricing-container {
    padding: 1rem;
  }

  .service-categories {
    flex-direction: column;
  }

  .price-details {
    grid-template-columns: 1fr;
  }

  .summary-header,
  .summary-row,
  .summary-total {
    grid-template-columns: 1fr;
    text-align: left;
  }

  .action-buttons {
    grid-template-columns: 1fr;
  }

  .main-title {
    font-size: 2rem;
  }
}

/* Checkbox Custom Style */
input[type="checkbox"] {
  width: 20px;
  height: 20px;
  border: 2px solid #1B365D;
  border-radius: 4px;
  cursor: pointer;
}

input[type="checkbox"]:checked {
  background-color: #1B365D;
  border-color: #1B365D;
}

/* Print Styles */
@media print {
  .pricing-container {
    padding: 0;
  }

  .service-categories,
  .enquiry-btn,
  .action-buttons {
    display: none;
  }

  .quote-summary {
    box-shadow: none;
    padding: 0;
  }
}

.enquiry-summary {
  margin-top: 2rem;
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 4px;
}

.enquiry-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid #eee;
}

.enquiry-status {
  color: #666;
  font-style: italic;
}

.enquiry-details {
  display: flex;
  flex-direction: column;
  gap: 5px;
}

.enquiry-time {
  font-size: 0.9em;
  color: #1B365D;
  font-weight: 500;
}

.price-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

/* 调整时间选择器样式 */
:deep(.el-date-picker) {
  width: 100%;
}

.service-time {
  color: #1B365D;
  font-size: 0.9rem;
}
</style>