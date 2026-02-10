$(function () {

  const q = getQueryParam('q') || '';
  let sort = getQueryParam('sort');
  let minPrice = getQueryParam('minPrice') || 0;
  let maxPrice = getQueryParam('maxPrice') || 500000;

  if (!q.trim()) {
    $('#searchTitle').text('검색');
    renderEmpty('검색어를 입력해 주세요.');
    return;
  }

  /* =========================
     인기순 기본 적용
  ========================= */

  if (!sort) {

    const newUrl =
      '/search?q=' +
      encodeURIComponent(q) +
      '&sort=POPULAR';

    location.replace(newUrl);
    return;
  }

  $('#searchTitle').text("‘" + q + "’ 검색 결과");

  setActiveSort(sort);

  /* =========================
     가격 슬라이더 초기 세팅
  ========================= */

  const $min = $('#priceMin');
  const $max = $('#priceMax');

  $min.val(minPrice);
  $max.val(maxPrice);

  updatePriceText();

  /* =========================
     검색 API 호출
  ========================= */

  $.ajax({
    url: '/api/search',
    method: 'GET',
    data: {
      q: q,
      sort: sort,
      minPrice: minPrice,
      maxPrice: maxPrice
    },
    success: function (res) {

      if (!res || res.success !== true) {
        renderEmpty('검색 결과를 불러오지 못했습니다.');
        return;
      }

      renderBrands(res.data.brands || []);
      renderProducts(res.data.products || []);
    },
    error: function (xhr) {
      console.log('search api error:', xhr.status, xhr.responseText);
      renderEmpty(xhr?.responseJSON?.error?.message || '서버 오류');
    }
  });

  /* =========================
     필터 버튼
  ========================= */

  $('.filter-btn').on('click', function () {

    const newSort = $(this).data('sort');

    location.href =
      '/search?q=' + encodeURIComponent(q) +
      '&sort=' + newSort +
      '&minPrice=' + $min.val() +
      '&maxPrice=' + $max.val();
  });

  /* =========================
     슬라이더 이벤트
  ========================= */

  $min.on('input', function () {

    if (Number($min.val()) > Number($max.val())) {
      $min.val($max.val());
    }

    updatePriceText();
  });

  $max.on('input', function () {

    if (Number($max.val()) < Number($min.val())) {
      $max.val($min.val());
    }

    updatePriceText();
  });

  $('input[type=range]').on('change', function () {

    location.href =
      '/search?q=' + encodeURIComponent(q) +
      '&sort=' + sort +
      '&minPrice=' + $min.val() +
      '&maxPrice=' + $max.val();
  });
});

/* =========================
   SORT UI
========================= */

function setActiveSort(sort) {

  $('.filter-btn').each(function () {

    const btnSort = $(this).data('sort');

    if (btnSort === sort) {
      $(this).addClass('active');
    } else {
      $(this).removeClass('active');
    }
  });
}

/* =========================
   PRICE TEXT
========================= */

function updatePriceText() {

  $('#priceMinText').text(formatPrice($('#priceMin').val()) + '원');
  $('#priceMaxText').text(formatPrice($('#priceMax').val()) + '원');
}

/* =========================
   RENDER
========================= */

function renderBrands(brands) {

  const $grid = $('#brandGrid');
  $grid.empty();

  if (!brands.length) {
    $grid.append(`<div style="color:#888; font-size:13px;">일치하는 브랜드가 없습니다.</div>`);
    return;
  }

  brands.forEach(b => {

    const thumb = b.logoUrl
      ? `<img src="${escapeHtml(b.logoUrl)}" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>${escapeHtml(b.name)}</span>`;

    const card = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-name">${escapeHtml(b.name)}</div>
        <div class="product-brand">바로가기</div>
      </div>
    `;

    $(card)
      .on('click', () => location.href = '/brands/' + b.slug)
      .appendTo($grid);
  });
}

function renderProducts(products) {

  const $grid = $('#productGrid');
  $grid.empty();

  if (!products.length) {
    $grid.append(`<div style="color:#888;">상품이 없습니다.</div>`);
    return;
  }

  products.forEach(p => {

    const thumb = p.thumbnailUrl
      ? `<img src="${escapeHtml(p.thumbnailUrl)}" style="width:100%; height:100%; object-fit:cover;">`
      : `<span>NO IMAGE</span>`;

    const card = `
      <div class="product-card">
        <div class="product-thumb">
          <div class="product-thumb-inner">${thumb}</div>
        </div>
        <div class="product-brand">${escapeHtml(p.brandName)}</div>
        <div class="product-name">${escapeHtml(p.name)}</div>
        <div class="product-price">
          ${formatPrice(p.price)}원
        </div>
      </div>
    `;

    $(card)
      .on('click', () => location.href = '/products/' + p.slug)
      .appendTo($grid);
  });
}

/* =========================
   UTIL
========================= */

function renderEmpty(msg) {
  $('#brandGrid').html(`<div style="color:#888;">${escapeHtml(msg)}</div>`);
  $('#productGrid').empty();
}

function getQueryParam(key) {
  return new URLSearchParams(location.search).get(key);
}

function formatPrice(price) {
  const n = Number(price);
  return Number.isFinite(n) ? n.toLocaleString('ko-KR') : price;
}

function escapeHtml(str) {
  if (!str) return '';
  return str
    .replaceAll('&','&amp;')
    .replaceAll('<','&lt;')
    .replaceAll('>','&gt;')
    .replaceAll('"','&quot;')
    .replaceAll("'",'&#039;');
}
