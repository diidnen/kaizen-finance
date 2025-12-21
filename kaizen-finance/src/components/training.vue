<template>
    <div class="training-container">
      <h1 class="main-title">International Payment Services</h1>
  
      <section class="training-section">
        <h2>Paying from the UK to Rest of World</h2>
        <div class="content-wrapper">
          <p>Learn how to make international payments efficiently and securely.</p>
          <a href="https://www.loom.com/share/f4965758a6b04b17920f87335de60f28?sid=5fa35dd3-3039-4eaa-9b30-5eb82565c774" target="_blank" class="link-button">
            <i class="fas fa-external-link-alt"></i>
            View Payment Guide
          </a>
          <p class="note">Important: Large payments (more than 20k GBP) will need to be processed via manual funding.</p>
        </div>
      </section>
  
      <section class="training-section">
        <h2>Paying from Rest of World into UK</h2>
        <div class="content-wrapper">
          <p>This service works for payments outside of the UK.</p>
          <a href="https://t.sidekickopen27.com/Ctc/ZU+23284/d10Br404/Jl22-6qcW7lCdLW6lZ3mVW5QGzlz5rG1_TW4Z8XmP7kLbmzW7b-pwy1YpWnMW3ZjXvy1TWGfZW25zh8f4LHLV3VN3f-t87bfzYW1fyhNQ7qYn05W6c3-XY5-Pj8cW9fH5ry57L8XFW6q2qWZ512vPPW3HGpck2hdB1yW2vtK2r75jydpW3d1hdw2KwnKnW2fQj0848rVt6W8pMQ251jJCgtW2BJzQ075Bgs5W44cG-N2GhgMTW64rSLR4gT_7SW5Mn46L72-6plVV5HHN43jj4TN863tms2ZSD9N6yjBsW_QYRyW6rzD1-5SXdMSN3s15N0B_Zqlf2PHGY804" target="_blank" class="link-button">
            <i class="fas fa-external-link-alt"></i>
            View Payment Guide
          </a>
          <div class="currency-section">
            <h3>Supported Currencies</h3>
            <div class="currency-grid">
              <span v-for="currency in currencies" :key="currency" class="currency-item">
                {{ currency }}
              </span>
            </div>
          </div>
        </div>
      </section>
  
      <section class="training-section">
        <h2>Receiving Payments from the UK</h2>
        <div class="content-wrapper">
          <p>Ideal for foreign businesses with UK customers. Collect payments via Crezco with competitive exchange rates.</p>
          <a href="https://t.sidekickopen27.com/Ctc/ZU+23284/d10Br404/Jl22-6qcW7lCdLW6lZ3lLW6cR3Rl58l_58W7-Lzll1V3FkzW6WFTN92S0Xk4W8cSY225cMb7kN1nKCddfXm6cW5lhyMl88RjRWW165hWL78r_JYW4LtJCr45ywk4W28Vxp34zQf-cW56qhKt4XDSPcW1Qtz6_31JRsMW7fw5TC2NgW6-VY2LFk7VNGXfW8Dw8BW3YSCHxW6xLhnw1-X9YMW209b1z3Mdm-YW65-k3h7RSK9yW67LPMm1gt-cXVLTxjK2rg7FfW6R6VKm7Jh5q2W1ctgjN5S-hpNW8qPKgM3MZXq1W5FsKSK444YR7W7g7ZM01cnKMff4bGYKd04" target="_blank" class="link-button">
            <i class="fas fa-external-link-alt"></i>
            View Payment Guide
          </a>
        </div>
      </section>
    </div>
  </template>
  
  <script>
  export default {
    name: 'Services',
    data() {
      return {
        currencies: [
          'AUD', 'BHD', 'BGN', 'CAD', 'CNY', 'CZK', 'DKK', 'HKD', 'HUF', 
          'ILS', 'JPY', 'KES', 'KWD', 'MXN', 'NZD', 'NOK', 'OMR', 'PLN', 
          'QAR', 'RON', 'SAR', 'SGD', 'ZAR', 'SEK', 'CHF', 'THB', 'TRY', 
          'UGX', 'AED', 'USD'
        ]
      }
    }
  }
  </script>
  
  <style scoped>
  .training-container {
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
  

  .training-section {
    background: #FFFFFF;
    border-radius: 4px;
    padding: 2.5rem;
    margin-bottom: 2rem;
    box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
    border-left: 5px solid #1B365D;
  }
  
  .training-section h2 {
    color: #1B365D;
    margin-bottom: 1.5rem;
    font-size: 1.5rem;
    font-weight: 600;
  }
  
  .content-wrapper {
    padding: 1rem 0;
  }
  
  .link-button {
    display: inline-flex;
    align-items: center;
    background: #1B365D;
    color: white;
    padding: 0.8rem 1.5rem;
    border-radius: 4px;
    text-decoration: none;
    margin: 1rem 0;
    transition: all 0.3s ease;
    font-weight: 500;
  }
  
  .link-button i {
    margin-right: 8px;
  }
  
  .link-button:hover {
    background: #2C5282;
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(27, 54, 93, 0.2);
  }
  
  .note {
    background: #F8F9FA;
    border-left: 4px solid #1B365D;
    padding: 1rem;
    margin: 1rem 0;
    font-size: 0.95rem;
    color: #2F4F4F;
  }
  
  .currency-section {
    margin-top: 2rem;
  }
  
  .currency-section h3 {
    color: #1B365D;
    margin-bottom: 1rem;
    font-size: 1.2rem;
  }
  
  .currency-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(90px, 1fr));
    gap: 0.8rem;
  }
  
  .currency-item {
    background: #F8F9FA;
    padding: 0.6rem;
    border-radius: 4px;
    text-align: center;
    font-weight: 500;
    font-size: 0.9rem;
    color: #1B365D;
    border: 1px solid #E2E8F0;
    transition: all 0.2s ease;
  }
  
  .currency-item:hover {
    background: #EDF2F7;
    border-color: #1B365D;
  }
  
  p {
    line-height: 1.6;
    color: #2F4F4F;
    margin-bottom: 1rem;
  }
  
  @media (max-width: 768px) {
    .training-container {
      padding: 1rem;
    }
    
    .training-section {
      padding: 1.5rem;
    }
    
    .main-title {
      font-size: 2rem;
    }
  }
  </style>