// Copyright (c) 2001 The Wilson Partnership.
// All Rights Reserved.
// @(#)MinMLSocketServer.java, 0.1, 6th July 2001
// Author: John Wilson - tug@wilson.co.uk

package uk.co.wilson.net;

/*
Copyright (c) 2001 John Wilson (tug@wilson.co.uk).
All rights reserved.
Redistribution and use in source and binary forms,
with or without modification, are permitted provided
that the following conditions are met:

Redistributions of source code must retain the above
copyright notice, this list of conditions and the
following disclaimer.

Redistributions in binary form must reproduce the
above copyright notice, this list of conditions and
the following disclaimer in the documentation and/or
other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY JOHN WILSON ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL JOHN WILSON
BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING
IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE
*/

import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

import java.io.InterruptedIOException;
import java.io.IOException;

public abstract class MinMLSocketServer{
  public MinMLSocketServer(final ServerSocket serverSocket,
                           final int minWorkers,
                           final int maxWorkers,
                           final int workerIdleLife)
  {
    this.serverSocket = serverSocket;
    this.minWorkers = Math.max(minWorkers, 1);
    this.maxWorkers = Math.max(this.minWorkers, maxWorkers);
    this.workerIdleLife = workerIdleLife;
  }

  public void start() {
    getNewWorker().run();
  }

  public synchronized void shutDown() throws IOException {
    this.serverSocket.close();
  }

  public int getPortNumber() {
    return this.serverSocket.getLocalPort();
  }

  protected abstract Worker makeNewWorker();

  private void setsocketTimeout(final int timeout) {
    try {
      this.serverSocket.setSoTimeout(timeout);
    }
    catch (final SocketException e) {
    }
  }

  private Worker getNewWorker() {
if (debug) System.out.println("Starting new thread: liveWorkerCount = " + this.liveWorkerCount + " workingWorkerCount = " + this.workingWorkerCount);
    if (this.liveWorkerCount++ == this.minWorkers)
      setsocketTimeout(this.workerIdleLife);

    return makeNewWorker();
  }

  private synchronized void startWork() {
    if (++this.workingWorkerCount == this.liveWorkerCount && this.liveWorkerCount < this.maxWorkers)
      new Thread(getNewWorker()).start();
if (debug) System.out.println("Thread starting work: liveWorkerCount = " + this.liveWorkerCount + " workingWorkerCount = " + this.workingWorkerCount);
  }

  private synchronized void endWork() {
    this.workingWorkerCount--;
if (debug) System.out.println("Thread ending work: liveWorkerCount = " + this.liveWorkerCount + " workingWorkerCount = " + this.workingWorkerCount);
  }

  private synchronized boolean workerMustDie() {
if (debug) System.out.println("Thread timing out socket read: liveWorkerCount = " + this.liveWorkerCount + " workingWorkerCount = " + this.workingWorkerCount);
    if (this.liveWorkerCount > this.minWorkers && this.liveWorkerCount != this.workingWorkerCount + 1) {
if (debug) System.out.println("Thread commits suicide");
      workerDies();

      return true;
    }

    return false;
  }

  private synchronized void workerDies() {
    if (--this.liveWorkerCount == this.minWorkers)
      setsocketTimeout(0);
if (debug) System.out.println("Thread dying: liveWorkerCount = " + this.liveWorkerCount + " workingWorkerCount = " + this.workingWorkerCount);
  }

  protectedb�%mG�)V@K�9�¤4w���r;��\�I({�C� 5���s�^I�"������'��������M���'vf��"���d;��P��k�?=�D��� L(v)�J��	?2�*�:�}���@�Q�v���%�O}������K7G���� o��V��=��E3��G]��ةJ�D��ZxyQ�Lt��'��~`H�PP=����?�9���������=~�7�X����0B��p���A����DBF�.x��D'�h�
$?��V��S��[�>�Tu�|���jT��P�{�w��}�
��<�f���״��_��p�t��@ٜnR���eIQ�~��#�;�LM<M��k��As���5�k�?D
z],!��l�3��N��I5��P6+��6�#���ugE�#�
��W/^�5����[�
�[��
��
� ��.8��z�m��Z�3P�BY[��SU>O��͂ FW�Ve���U�ʫ=���B�>r��"�P�L�oO ���4��dԺ�YL��L
M����d��D�c.6�y%��=��)��f
�TQO>���Ժ\}�=��_�˺nb�n�H���%���u�LW�g�A��nyF9[��~��۠y���`�籍G�_�U�w���ӴB�S��Wف�y�����cZ�b��֬�|��R��
�6���t�K%4�����3p^�_�Ӝ�?3�`�4�@�%�V�Ƅ�bon���RP΍��$�1W��J���x�C{9�EÑ�o8�;�qt��C3�'\��ѳ����=�-t��`�)��V8ǌ X�$�
c?�q�DV�@�ԋ'\c9�Nν)��S� J2��6Q��iw�ۅ}�D����`ؔ�hl���1R����G���m.S~VC�("��0X�3����D�|�o��8�(a��j�ǅ����A���X��/�Hf��Pk�
�=������X/�W�oB=�ޥ��69j�ϋ��ߜ������ϛC�c�������u�,)���#�^U�"B��).]�S�<���91y�<͛�{�tg���h������W /����r>��(%�V�:��U�l���th�F�Iޮ3�����^�H��YH��GV�8���|h޸�.	�%6��->�X-���x֘�*���!?��u&׿k΄�N��{�P}���7� �zc_��۱�X����������U�����U�I�uqͦ����☬p4_�.-H��3�
xh
I�1R�z��]����n�Zd��%+���x_g����W���~'����e��ҪZ~Ȁ����b����A-/jB��H�Zb�z
�&$	#�U5��ZI"Þ>�B�x٬�l�6+ �[�E�����#�Ӫ)g�}5P�sIP�,+������ J����
�?�#ꐓ�
^�&�?϶⽣�L���֨�p��>���̄��7K��6�K���T橩xE���D��"��@�͇7���� � �����J�@�W�~�F��R�s\�}�>C:qZ��}P{�����{x c}������gd��x��W�/w�Qv-�^e�ٔ�ҟ����;�p&��[$�7T��=˞zؖ�1w�W|��W�hW�o�im�#�_҅Cd��0o[fv{�y�����ە�6+��v4�`�%��F��]�����C���7R�{ٌ�7�_V�f�K��ӂ��S���͆�1�;;Z�ZVo�%��*�"oC'�<���r�Ҟk6E�;
?��S9<�����i�