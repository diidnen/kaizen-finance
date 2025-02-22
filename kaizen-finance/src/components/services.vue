<template>
  <div class="services-container">
    <h1 class="main-title">Our Services</h1>

    <!-- Core Services Section -->
    <section class="services-section">
      <h2>Core Business</h2>
      <div class="core-services">
        <div class="service-card">
          <i class="fas fa-calculator"></i>
          <h3>Accounting</h3>
          <p>Professional accounting services tailored to your business needs</p>
        </div>
        <div class="service-card">
          <i class="fas fa-handshake"></i>
          <h3>Business Consultancy</h3>
          <p>Expert guidance to help your business grow and succeed</p>
        </div>
      </div>
    </section>

    <!-- Additional Services Section -->
    <section class="services-section">
      <h2>Additional Services</h2>
      <div class="tabs">
        <div class="tab-buttons">
          <button 
            v-for="(tab, index) in tabs" 
            :key="index"
            :class="[
              'tab-button', 
              { active: currentTab === index },
              { 'font-bold': [0, 1, 2, 3,4,5, 6, 7].includes(index) }
            ]"
            @click="currentTab = index"
          >
            {{ tab.title }}
          </button>
        </div>

        <div class="tab-content">
          <div v-show="currentTab === 0" class="tab-pane">
            <h3>Virtual Office</h3>
            <p>Professional mail forwarding services in partnership with 
              <a href="https://www.hoxtonmix.com/?gad_source=1&gclid=Cj0KCQiAvvO7BhC-ARIsAGFyToX8WNOR5XYoyQiZsKEjxbMlHN9Mg7qm-9Du1rYwFeXRFMbbCXXLwLoaAqoaEALw_wcB" target="_blank" class="company-link">
                <strong>Hoxton Mix</strong>
              </a>
            </p>
          </div>

          <div v-show="currentTab === 1" class="tab-pane">
            <h3>Business Loan</h3>
            <p>Flexible financing solutions through our trusted partners 
              <a href="https://www.iwoca.co.uk/" target="_blank" class="company-link">
                <strong>IWOCA</strong>
              </a> 
              and 
              <a href="https://www.approvity.com/" target="_blank" class="company-link">
                <strong>Approvity</strong>
              </a>
            </p>
          </div>

          <div v-show="currentTab === 2" class="tab-pane">
            <h3>R&D Tax Credits</h3>
            <p>Maximize your R&D tax relief benefits with our expert guidance</p>
          </div>

          <div v-show="currentTab === 3" class="tab-pane">
            <h3>Lending Solutions</h3>
            <p>Comprehensive mortgage, special lending and commercial lending services</p>
          </div>

          <div v-show="currentTab === 4" class="tab-pane">
            <h3>Cloud Accounting</h3>
            <p>Modern cloud-based accounting solutions for efficient business management</p>
            
            <div class="pricing-info">
              <p class="discount-text">
                Price is on <a href="https://www.xero.com/uk/pricing/" target="_blank" class="vendor-link">Xero</a> 
                and <a href="https://quickbooks.intuit.com/uk/pricing/" target="_blank" class="vendor-link">QuickBooks</a> website
              </p>
              <div class="discount-badge">
                <span class="discount-amount">10%</span>
                <span class="discount-label">Ongoing Discount</span>
              </div>
            </div>
          </div>

          <div v-show="currentTab === 5" class="tab-pane">
            <h3>Business Banking</h3>
            <p>Strategic banking solutions for your business needs</p>
          </div>

          <div v-show="currentTab === 6" class="tab-pane">
            <h3>Payment Solutions</h3>
            <p>Integrated payment solutions including 
              <a href="https://www.paypal.com/" target="_blank" class="company-link">
                <strong>PayPal</strong>
              </a> 
              and 
              <a href="https://sumup.co.uk/" target="_blank" class="company-link">
                <strong>SumUp</strong>
              </a>
            </p>
          </div>

          <div v-show="currentTab === 7" class="tab-pane">
            <h3>Crezco</h3>
            <p>Crezco is a platform that allows you to buy and sell foreign currency</p>
            
            <p class="subtitle">Why choose Crezco?</p>
            <ul>
              <li>No hidden fees, cheaper than banks (with 0.3% to 0.5% rate compared to 2-3% and 25-50 fees in banks)</li>
              <li>Easy safe payment method</li>
            </ul>
            
            <p class="subtitle">How to use Crezco?</p>
            <div class="steps">
              <p>Sign up and verify your account</p>
              <p>Add your bank account</p>
              <p>Buy or sell foreign currency</p>
            </div>

            <div class="button-container">
              <button @click="handleForeignExchange" class="left-btn">Click here to fill in the form</button>
              <button @click="handleForeignExchangeVideo" class="right-btn">Click here to see the training video</button>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script>
export default {
  name: 'Services',
  data() {
    return {
      currentTab: 0,
      tabs: [
        { title: 'Virtual Office' },
        { title: 'Business Loan' },
        { title: 'R&D' },
        { title: 'Lending' },
        { title: 'Cloud Accounting' },
        { title: 'Business Banking' },
        { title: 'Payment Solutions' },
        { title: 'FX Services' }
      ]
    }
  },
  mounted() {
    // Load Approvity script
    const script = document.createElement('script')
    script.src = 'https://iframe.approvity.com/apply-js'
    document.body.appendChild(script)
  },
  methods: {
    handleForeignExchange() {
      window.open('https://airtable.com/appRiJJslMAUaNf4a/shrYA50W0ejCzY1qA', '_blank');
    },
    handleForeignExchangeVideo() {
      this.$router.push('/training');
    }
  }
} 
</script>

<style scoped>
.services-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  color: #2F4F4F;
}

.main-title {
  color: #1B365D;
  text-align: center;
  margin-bottom: 3rem;
  font-size: 2.5rem;
  font-weight: 600;
  position: relative;
}

.main-title::after {
  content: '';
  display: block;
  width: 60px;
  height: 3px;
  background: #1B365D;
  margin: 1rem auto;
}

.services-section {
  margin-bottom: 4rem;
}

.services-section h2 {
  color: #1B365D;
  margin-bottom: 2rem;
  font-size: 1.8rem;
}

.core-services {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 2rem;
  margin-bottom: 3rem;
}

.service-card {
  background: white;
  border-radius: 15px;
  padding: 2rem;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  transition: transform 0.3s ease;
}

.service-card:hover {
  transform: translateY(-5px);
}

.service-card i {
  font-size: 2.5rem;
  color: #1B365D;
  margin-bottom: 1rem;
}

h3 {
  color: #2d3436;
  font-size: 2rem;
  margin-bottom: 1.5rem;
  font-weight: 600;
}

p {
  color: #636e72;
  line-height: 1.6;
  margin-bottom: 1rem;
  font-size: 1.1rem;
}

ul {
  margin: 1.5rem 0;
  padding-left: 1.2rem;
}

li {
  color: #636e72;
  margin-bottom: 0.8rem;
  line-height: 1.6;
  position: relative;
  padding-left: 1rem;
}

li::before {
  content: "•";
  color: #007bff;
  font-weight: bold;
  position: absolute;
  left: -1rem;
}

.tabs {
  background: #FFFFFF;
  border-radius: 4px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.tab-buttons {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  padding: 1rem;
  background: #F8F9FA;
  border-radius: 4px 4px 0 0;
}

.tab-button {
  padding: 0.8rem 1.5rem;
  border: none;
  background: none;
  color: #2F4F4F;
  cursor: pointer;
  border-radius: 4px;
  transition: all 0.3s ease;
}

.tab-button.active {
  background: #1B365D;
  color: white;
}

.tab-content {
  padding: 2rem;
}

.tab-pane {
  animation: fadeIn 0.3s ease;
}

.tab-pane h3 {
  color: #1B365D;
  margin-bottom: 1rem;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@media (max-width: 768px) {
  .services-container {
    padding: 1rem;
  }
  
  .tab-buttons {
    flex-direction: column;
  }
  
  .tab-button {
    width: 100%;
    text-align: left;
  }
}

.tab-button.font-bold {
  font-weight: 700;
  color: #1B365D;
}

.tab-button.font-bold.active {
  background: #1B365D;
  color: white;
  font-weight: 700;
}

.company-link {
  text-decoration: none;
  color: #1B365D;
  transition: color 0.3s ease;
}

.company-link:hover {
  color: #3498db;
  text-decoration: underline;
}

.company-link strong {
  font-weight: 700;
}

.subtitle {
  color: #1B365D;
  font-weight: 600;
  margin-top: 1rem;
  margin-bottom: 0.5rem;
}

.steps {
  margin-top: 2rem;
  counter-reset: step;
}

.steps p {
  position: relative;
  padding-left: 2.5rem;
  margin-bottom: 1rem;
}

.steps p::before {
  counter-increment: step;
  content: counter(step);
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 1.8rem;
  height: 1.8rem;
  background: #090909;
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 0.9rem;
  font-weight: bold;
}

button {
  background: #2d3436;
  color: white;
  border: none;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  margin: 1rem 0;
  float: right;
  clear: both;
}

button:hover {
  background: #0056b3;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 123, 255, 0.3);
}

button:active {
  transform: translateY(0);
}

/* 添加一个清除浮动的辅助类 */
.clearfix::after {
  content: "";
  display: table;
  clear: both;
}

.pricing-info {
  background: #f8f9fa;
  border-radius: 10px;
  padding: 1.5rem;
  margin-top: 1rem;
  border: 2px solid #e9ecef;
  position: relative;
}

.discount-text {
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.vendor-link {
  color: #2d3436;
  text-decoration: none;
  font-weight: 600;
  border-bottom: 2px solid #2d3436;
  transition: all 0.3s ease;
}

.vendor-link:hover {
  opacity: 0.7;
}

.discount-badge {
  display: inline-flex;
  flex-direction: column;
  align-items: center;
  background: #2d3436;
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 8px;
  margin-top: 1rem;
}

.discount-amount {
  font-size: 1.5rem;
  font-weight: 700;
  line-height: 1;
}

.discount-label {
  font-size: 0.9rem;
  margin-top: 0.2rem;
}

.left-btn {
  float: left;
  clear: both;
}

.right-btn {
  float: right;
  clear: both;
}

.button-container {
  display: flex;
  justify-content: space-between;
  margin-top: 2rem;
  width: 100%;
}

.left-btn, .right-btn {
  margin: 0;
}
</style>
