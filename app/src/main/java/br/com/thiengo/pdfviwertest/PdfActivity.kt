package br.com.thiengo.pdfviwertest

import android.graphics.Canvas
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import br.com.thiengo.pdfviwertest.domain.Doc
import com.github.barteksc.pdfviewer.listener.*
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import kotlinx.android.synthetic.main.activity_pdf.*


class PdfActivity :
        AppCompatActivity(),
        OnPageChangeListener,
        OnLoadCompleteListener,
        OnRenderListener,
        OnPageScrollListener,
        OnDrawListener,
        OnErrorListener {

    var doc: Doc? = null
    var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)

        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()?.setDisplayShowHomeEnabled(true)

        doc = intent.getParcelableExtra(Doc.DOC_KEY)

        // PERMISSÃO EM TEMO DE EXECUÇÃO E NO ANDROIDMANIFEST SERÁ NECESSÁRIA
        //val file = File(Environment.getExternalStorageDirectory(), "DCIM/${doc?.path}")
        //Log.i("Log", "${Environment.getExternalStorageDirectory()} - ${file.path}");

        pdfView
            //.fromUri( Uri.fromFile( file ) )
            //.fromFile(file)
            .fromAsset( doc?.path )
            .defaultPage( doc?.getActualPageSP(this) ?: 0 )
            .onPageChange(this)
            .enableAnnotationRendering(true)
            .onLoad(this)
            .scrollHandle( DefaultScrollHandle(this) ) // PARA APRESENTAÇÃO DO NÚMERO DE PÁGINA NA TELA
            .enableSwipe(true) // CASO QUEIRA BLOQUEAR A MUDANÇA DE PÁGINA, COLOQUE false AQUI
            .swipeHorizontal(true) // O PADRÃO É O SWIPE VERTICAL
            .enableDoubletap(true) // PARA BLOQUEAR O ZOOM COM TOUCHES, COLOQUE false AQUI
            .enableAntialiasing(true) // MELHORA RENDERIZAÇÃO EM TELAS PEQUENAS
            .enableAnnotationRendering(true) // RENDERIZA TAMBÉM DADOS DE ANOTAÇÃO (MARCAÇÕES E COMENTÁRIOS, POR EXEMPLO)
            .onRender(this)
            //.pages(0, 1, 120) // CASO SEJA NECESSÁRIO DEFINIR AS PÁGINAS LIBERADAS, COLOQUE AS NUMERAÇÕES DELAS AQUI, INICIANDO DE 0
            .onPageScroll(this)
            //.password(null) // PARA QUANDO O PDF TEM SENHA DE ACESSO
            .onDraw(this) // PERMITE O DESENHO DE ALGO NA PÁGINA ATUAL
            .onError(this)
            .load();
    }

    override fun onResume() {
        super.onResume()
        toolbar?.title = doc?.language
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }



    override fun loadComplete(nbPages: Int) {
        //Log.i("log_data", "loadComplete($nbPages)");
    }

    override fun onPageChanged(page: Int, pageCount: Int) {
        //Log.i("log_data", "onPageChanged($page, $pageCount) ${doc?.getActualPageSP(this) ?: 0}");
        doc?.saveActualPageSP(this, page)
    }

    override fun onInitiallyRendered(nbPages: Int, pageWidth: Float, pageHeight: Float) {
        //Log.i("log_data", "onInitiallyRendered($nbPages, $pageWidth, $pageHeight)");
        pdfView.fitToWidth( doc?.getActualPageSP(this) ?: 0 )
    }

    override fun onPageScrolled(page: Int, positionOffset: Float) {
        //Log.i("log_data", "onPageScrolled($page, $positionOffset)");
    }

    override fun onLayerDrawn(canvas: Canvas?, pageWidth: Float, pageHeight: Float, displayedPage: Int) {
        //Log.i("log_data", "onLayerDrawn($pageWidth, $pageHeight, $displayedPage)");

        /*val myPaint = Paint()
        myPaint.setColor(Color.RED)
        myPaint.setStyle(Paint.Style.STROKE)
        myPaint.setStrokeWidth(3F)
        canvas?.drawRect(20F, 20F, 100F, 100F, myPaint)*/
    }

    override fun onError(t: Throwable?) {
        //Log.i("log_data", "onError($t)");
    }
}
